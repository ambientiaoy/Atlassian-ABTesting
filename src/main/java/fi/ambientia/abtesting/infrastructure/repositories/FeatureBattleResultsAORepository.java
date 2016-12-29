package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import fi.ambientia.atlassian.properties.PluginProperties;

import java.util.ArrayList;
import java.util.List;

public class FeatureBattleResultsAORepository implements FeatureBattleResults {


    public FeatureBattleResultsAORepository(ActiveObjects ao, PluginProperties properties) {

    }

    @Override
    public List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier experiment) {
        return new ArrayList<>();
    }
}
