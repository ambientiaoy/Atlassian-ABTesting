package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.IdResolver;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.PageObject;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattle;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.atlassian.properties.PluginProperties;
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
                userExperimentAO.setFeatureBattleId( featureBattleIdentifier.getIdentifier() );
                userExperimentAO.setExperimentType( experiment.type() );
                userExperimentAO.save();

            };
        };
    }

    protected UserExperimentAO getUserExperimentAO(String userId, String featureBattleId) {
        Optional<UserExperimentAO> userExperimentAO1 = EnsureOnlyOneAOEntityExists.execute(ao, UserExperimentAO.class, "USER_ID = ? AND FEATURE_BATTLE_id = ?", userId, featureBattleId );

        return userExperimentAO1.orElseGet(() -> ao.create(UserExperimentAO.class));
    }

    @Override
    public Optional<FeatureBattle> getFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier) {
        Optional<FeatureBattleAO> featureBattleAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", featureBattleIdentifier.getIdentifier());
        return featureBattleAO.map( entity -> new FeatureBattle(new FeatureBattleIdentifier( entity.getFeatureBattleId() )));
    }

    @Override
    public Experiment randomBattleResultFor(FeatureBattleIdentifier identifier) {
        Optional<FeatureBattleAO> featureBattleAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", identifier.getIdentifier());

        Optional<Experiment.Type> type = featureBattleAO.map(featureBattleAO1 -> Experiment.randomize(random, featureBattleAO1.getThreshold(), identifier));

        Optional<ExperimentAO> experimentAO = featureBattleAO.flatMap(
                (fbAO) -> type.flatMap(
                        (_type) -> EnsureOnlyOneAOEntityExists.execute(ao, ExperimentAO.class, "FEATURE_BATTLE_ID = ? AND EXPERIMENT_TYPE = ?", fbAO.getID(), _type.name()))
        );

        return experimentAO.map(
                (_ao) -> Experiment.forType( _ao.getExperimentType() ).withIdentifier( _ao.getExperimentId(),
                        new PageObject(properties.propertyOrDefault("default.abtest.space.key", "ABTESTS"), _ao.getPage() ) )
        ).orElse(Experiment.missingExperiment() );
    }


}
