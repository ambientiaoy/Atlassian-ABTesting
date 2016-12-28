package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.IdResolver;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattle;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Repository
public class FeatureBattleAORepository implements FeatureBattleRepository{

    public static final int DEFAULT_THRESHOLD = 10;
    private final ActiveObjects ao;
    private final PluginProperties properties;
    private final ExperimentAORepository experimentAORepository;
    private Random random;

    @Autowired
    public FeatureBattleAORepository(@ComponentImport ActiveObjects ao, PluginProperties properties, ExperimentAORepository experimentAORepository) {
        this.ao = ao;
        this.properties = properties;
        this.experimentAORepository = experimentAORepository;
        random = new Random();
    }

    @Override
    public IdResolver createFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier) {
        Optional<FeatureBattleAO> featureBattleAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "FEATURE_BATTLE_ID = ?", featureBattleIdentifier.getIdentifier());
        if(featureBattleAO.isPresent()){
            return () -> featureBattleAO.get().getID();
        }
        FeatureBattleAO experimentAO = ao.create(FeatureBattleAO.class);
        experimentAO.setFeatureBattleId( featureBattleIdentifier.getIdentifier() );
        experimentAO.setThreshold( properties.propertyOrDefault("experiment.default.threshold", DEFAULT_THRESHOLD));
        experimentAO.save();

        return () -> experimentAO.getID();
    }

    @Override
    public void setThreshold(Integer featureBattleIdentifier, int threshold) {
        Optional<FeatureBattleAO> optional = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "ID = ? ", featureBattleIdentifier);
        optional.ifPresent(( featureBattleAO ) -> {
            featureBattleAO.setThreshold( threshold );
            featureBattleAO.save();
        });
    }

    @Override
    public List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier experiment) {
        // TODO AkS implement!
        return new ArrayList<>();
    }

    @Override
    public CreateNewFeatureBattleFor newFeatureBattleFor(FeatureBattleIdentifier featureBattleIdentifier) {
        Optional<FeatureBattleAO> fbAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", featureBattleIdentifier.getIdentifier());
        List<ExperimentAO> experimentAOs = Arrays.asList(fbAO.get().getExperiments());

        return ( user) -> {
            return ( experiment ) -> {
                Optional<ExperimentAO> experimentAOOptional = experimentAOs.stream().
                        filter((experimentAO -> experimentAO.getExperimentType().equals(experiment.type()))).
                        findFirst();

                UserExperimentAO userExperimentAO = getUserExperimentAO(user.getIdentifier(), featureBattleIdentifier.getIdentifier());
                // FIXME AkS: I See a null here!
                userExperimentAO.setExperiment( experimentAOOptional.orElse( null ) );
                userExperimentAO.setUserId( user.getIdentifier() );
                userExperimentAO.setExperimentType( experiment.type() );
                userExperimentAO.save();

            };
        };
    }

    protected UserExperimentAO getUserExperimentAO(String userId, String experimentId) {
        Optional<ExperimentAO> experimentOptional = EnsureOnlyOneAOEntityExists.execute(ao, ExperimentAO.class, "EXPERIMENT_ID = ? ", experimentId);
        Integer experimentID = experimentOptional.isPresent() ? experimentOptional.get().getID() : -1 ;
        Optional<UserExperimentAO> userExperimentAO1 = EnsureOnlyOneAOEntityExists.execute(ao, UserExperimentAO.class, "USER_ID = ? AND EXPERIMENT_ID = ?", userId, experimentID );

        return userExperimentAO1.orElseGet(() -> ao.create(UserExperimentAO.class));
    }

    @Override
    public Optional<FeatureBattle> getFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier) {
        Optional<FeatureBattleAO> featureBattleAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", featureBattleIdentifier.getIdentifier());
        return featureBattleAO.map( entity -> new FeatureBattle(new FeatureBattleIdentifier( entity.getFeatureBattleId() )));
    }

    @Override
    public Experiment randomBattleResultFor(FeatureBattleIdentifier identifier) {
        Optional<FeatureBattleAO> experimentAOOptional = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", identifier.getIdentifier());

        Optional<Experiment> experiment = experimentAOOptional.map(featureBattleAO -> Experiment.randomize(random, featureBattleAO.getThreshold(), identifier));

        return experiment.orElse(
                Experiment.randomize(random, properties.propertyOrDefault("feature.battle.default.win", DEFAULT_THRESHOLD), identifier)
        );
    }


}
