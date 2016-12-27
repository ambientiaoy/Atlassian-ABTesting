package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattle;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.Entity;
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
    private Random random;

    @Autowired
    public FeatureBattleAORepository(@ComponentImport ActiveObjects ao, PluginProperties properties) {
        this.ao = ao;
        this.properties = properties;
        random = new Random();
    }

    @Override
    public void createFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier) {
        Optional<FeatureBattleAO> featureBattleAO = find(FeatureBattleAO.class, "FEATURE_BATTLE_ID = ?", featureBattleIdentifier.getIdentifier());
        if(featureBattleAO.isPresent()){
            return;
        }

        FeatureBattleAO experimentAO = ao.create(FeatureBattleAO.class);
        experimentAO.setFeatureBattleId( featureBattleIdentifier.getIdentifier() );
        experimentAO.setThreshold( properties.propertyOrDefault("experiment.default.threshold", DEFAULT_THRESHOLD));
        experimentAO.save();
    }

    @Override
    public void setThreshold(FeatureBattleIdentifier featureBattleIdentifier, int threshold) {
        Optional<FeatureBattleAO> optional = find(FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", featureBattleIdentifier.getIdentifier());
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
    public CreateNewFeatureBattleFor newFeatureBattleFor(FeatureBattleIdentifier experiment) {
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
    public Optional<FeatureBattle> getFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier) {
        Optional<FeatureBattleAO> featureBattleAO = find(FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", featureBattleIdentifier.getIdentifier());
        return featureBattleAO.map( entity -> new FeatureBattle(new FeatureBattleIdentifier( entity.getFeatureBattleId() )));
    }

    @Override
    public Experiment randomBattleResultFor(FeatureBattleIdentifier identifier) {
        Optional<FeatureBattleAO> experimentAOOptional = find(FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", identifier.getIdentifier());

        Optional<Experiment> experiment = experimentAOOptional.map(featureBattleAO -> Experiment.randomize(random, featureBattleAO.getThreshold(), identifier));

        return experiment.orElse(
                Experiment.randomize(random, properties.propertyOrDefault("feature.battle.default.win", DEFAULT_THRESHOLD), identifier)
        );
    }


    protected <T extends Entity> Optional<T> find(Class<T> klazz, String clause, Object... params) {
        T[] ts = ao.find(klazz, Query.select().where(clause, params));
        if( ts.length > 1){
            Arrays.asList(ts).stream().forEach( entity -> ao.delete( entity ));
        }
        if( ts.length == 1){
            return Optional.of(ts[0]);
        }
        return Optional.empty();
    }
}
