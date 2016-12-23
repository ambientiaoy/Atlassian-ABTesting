package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
    public List<FeatureBattleResult> experimentsFor(ExperimentIdentifier experiment) {
        // TODO AkS implement!
        return new ArrayList<>();
    }

    @Override
    public ExperimentRandomizer experimentRandomizer(ExperimentIdentifier experimentIdentifier) {
        return () -> randomBattleResultFor( experimentIdentifier );
    }

    private Experiment randomBattleResultFor(ExperimentIdentifier identifier) {
        return randomIntLessThanDefaultFromProperties() ? new NewAndShiny(identifier) : new GoodOldWay(identifier);
    }

    private boolean randomIntLessThanDefaultFromProperties() {
        return random.nextInt(100) < properties.propertyOrDefault("feature.battle.default.win", 25);
    }

    @Override
    public CreateNewFeatureBattleFor newFeatureBattleFor(ExperimentIdentifier experiment) {


        return ( user) -> {
            return ( featureBattle ) -> {
                UserExperimentAO userExperimentAO = ao.create(UserExperimentAO.class);
                userExperimentAO.setExperimentId( experiment.getIdentifier() );
                userExperimentAO.setUserId( user.getIdentifier() );
                userExperimentAO.setExperiment( featureBattle.type() );

            };
        };
    }

    @Override
    public List<Experiment> randomizedExperimentsFor(UserIdentifier userientifier) {
        return new ArrayList<>();
    }
}
