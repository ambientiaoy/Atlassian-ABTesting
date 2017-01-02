package fi.ambientia.abtesting.infrastructure.repositories;

import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.feature_battles.UserExperimentRepository;
import fi.ambientia.atlassian.properties.PluginProperties;

import java.util.ArrayList;
import java.util.List;

public class UserExperimentAORepository implements UserExperimentRepository {
    private final SimpleActiveObjects sao;
    private final PluginProperties properties;

    public UserExperimentAORepository(SimpleActiveObjects sao, PluginProperties properties) {
        this.sao = sao;
        this.properties = properties;
    }

    @Override
    public List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier experimentIdentifier) {

        return new ArrayList<>();
    }
}
