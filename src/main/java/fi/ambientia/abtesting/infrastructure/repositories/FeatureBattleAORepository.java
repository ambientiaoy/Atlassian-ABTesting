package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.ExperimentRandomizer;
import fi.ambientia.abtesting.model.experiments.FeatureBattleRepository;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static fi.ambientia.abtesting.infrastructure.repositories.ExperimentRepository.getExperimentAO;

@Repository
public class FeatureBattleAORepository implements FeatureBattleRepository{

    private final ActiveObjects ao;
    private final PluginProperties properties;
    private Random random;

    @Autowired
    public FeatureBattleAORepository(@ComponentImport ActiveObjects ao, PluginProperties properties) {
        this.ao = ao;
        this.properties = properties;
        this.random = new Random();
    }

    @Override
    public List<FeatureBattleResult> featureBattleResultsFor(ExperimentIdentifier experiment) {
        // TODO AkS implement!
        return new ArrayList<>();
    }

    @Override
    public ExperimentRandomizer experimentRandomizer(ExperimentIdentifier experimentIdentifier) {
        return () -> randomBattleResultFor( experimentIdentifier );
    }

    // FIXME AkS: this is weird.
    public Experiment randomBattleResultFor(ExperimentIdentifier identifier) {
        Optional<ExperimentAO> experimentAOOptional = getExperimentAO(identifier, ao);

        Optional<Experiment> experiment = experimentAOOptional.map(experimentAO -> Experiment.randomize(random, experimentAO.getThreshold(), identifier));

        return experiment.orElse(
                Experiment.randomize(random, properties.propertyOrDefault("feature.battle.default.win", ExperimentRepository.DEFAULT_THRESHOLD), identifier)
        );
    }

    @Override
    public CreateNewFeatureBattleFor newFeatureBattleFor(ExperimentIdentifier experiment) {
        return ( user) -> {
            return ( featureBattle ) -> {
                UserExperimentAO userExperimentAO = getUserExperimentAO(user.getIdentifier(), experiment.getIdentifier());
                userExperimentAO.setExperimentId( experiment.getIdentifier() );
                userExperimentAO.setUserId( user.getIdentifier() );
                userExperimentAO.setExperimentType( featureBattle.type() );
                userExperimentAO.save();

            };
        };
    }

    protected UserExperimentAO getUserExperimentAO(String userId, String experimentId) {
        UserExperimentAO[] userExperimentAOs = ao.find(UserExperimentAO.class, Query.select().from(UserExperimentAO.class).where("USER_ID = ? AND EXPERIMENT_ID = ?", userId, experimentId));
        if( userExperimentAOs.length > 1){
            Arrays.asList(userExperimentAOs).stream().forEach( userExperimentAO -> ao.delete( userExperimentAO ));
        }
        if( userExperimentAOs.length == 1){
            return userExperimentAOs[0];
        }
        return ao.create(UserExperimentAO.class);
    }

    @Override
    public List<Experiment> experimentsForUser(UserIdentifier userientifier) {
        Query query = Query.select().from(UserExperimentAO.class).where("USER_ID = ?", userientifier.getIdentifier());
        UserExperimentAO[] userExperimentAOs = ao.find(UserExperimentAO.class, query);

        List<Experiment> experiments = Arrays.asList(userExperimentAOs).stream().
                map(userExperimentAO -> Experiment.forType(userExperimentAO.getExperimentType()).withIdentifier(userExperimentAO.getExperimentId())).
                collect(Collectors.toList());

        return experiments;
    }
}
