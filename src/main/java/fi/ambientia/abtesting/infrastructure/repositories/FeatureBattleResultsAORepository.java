package fi.ambientia.abtesting.infrastructure.repositories;

import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleEntity;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FeatureBattleResultsAORepository implements FeatureBattleResults {

    private final SimpleActiveObjects ao;
    private final PluginProperties properties;

    @Autowired
    public FeatureBattleResultsAORepository(@Qualifier("TransactionalActiveObject") SimpleActiveObjects ao, PluginProperties properties) {
        this.ao = ao;
        this.properties = properties;
    }

    @Override
    public List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier featureBattleIdentifier) {
        List<FeatureBattleResult> featureBattleResults = new ArrayList<>();

        FeatureBattleAO[] featureBattleAOs = ao.find(FeatureBattleAO.class, Query.select().where("FEATURE_BATTLE_ID = ? ", featureBattleIdentifier.getFeatureBattleId()));
        Optional<FeatureBattleAO> fb = Arrays.asList(featureBattleAOs).stream().findFirst();
        Optional<Integer> first = fb.map(_fb -> _fb.getID());

        if( fb.isPresent() ) {
            Integer feature_battle_id = first.orElse(-1);
            List<ExperimentAO> experimentAOs = Arrays.asList(fb.get().getExperiments());
            featureBattleResults = experimentAOs.stream().
                    map(
                            experimentAO -> new FeatureBattleResult(new UserIdentifier(""), ExperimentAORepository.buildExperiment(properties, experimentAO))
                    ).collect(Collectors.toList());

        }
        return featureBattleResults;
    }

    @Override
    public AddNewFeatureBattleResult newWinnerFor(FeatureBattleEntity featureBattleIdentifier) {
        return null;
    }

}
