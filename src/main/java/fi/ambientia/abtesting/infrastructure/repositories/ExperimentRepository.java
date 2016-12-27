package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.Optional;

public class ExperimentRepository {
    public static final int DEFAULT_THRESHOLD = 10;
    private final ActiveObjects ao;
    private final PluginProperties properties;

    public ExperimentRepository(ActiveObjects ao, PluginProperties properties) {
        this.ao = ao;
        this.properties = properties;
    }

    public void createExperiment(FeatureBattleIdentifier featureBattleIdentifier) {
        if( getExperimentAO(featureBattleIdentifier, ao).isPresent() ){
            return;
        }

        ExperimentAO experimentAO = ao.create(ExperimentAO.class);
        experimentAO.setExperimentId( featureBattleIdentifier.getIdentifier() );
        experimentAO.setThreshold( properties.propertyOrDefault("experiment.default.threshold", DEFAULT_THRESHOLD));
        experimentAO.save();
    }

    public void setThreshold(FeatureBattleIdentifier featureBattleIdentifier, int threshold) {
        Optional<ExperimentAO> optional = getExperimentAO(featureBattleIdentifier, ao);
        optional.ifPresent(( experimentAO ) -> {
            experimentAO.setThreshold( threshold );
            experimentAO.save();
        });
    }

    public static Optional<ExperimentAO> getExperimentAO(FeatureBattleIdentifier featureBattleIdentifier, ActiveObjects ao) {
        ExperimentAO[] userExperimentAOs = ao.find(ExperimentAO.class, Query.select().from(ExperimentAO.class).where("EXPERIMENT_ID = ? ", featureBattleIdentifier.getIdentifier()));
        if( userExperimentAOs.length > 1){
            Arrays.asList(userExperimentAOs).stream().skip(1).forEach(userExperimentAO -> ao.delete( userExperimentAO ));
            userExperimentAOs = ao.find(ExperimentAO.class, Query.select().from(ExperimentAO.class).where("EXPERIMENT_ID = ? ", featureBattleIdentifier.getIdentifier()));
        }
        if( userExperimentAOs.length == 1){
            return Optional.of( userExperimentAOs[0] );
        }
        return Optional.empty();
    }

}
