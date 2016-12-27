package fi.ambientia.abtesting.model.write;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;

public interface FeatureBattleInput {
    FeatureBattleIdentifier getFeatureBattleIdentifier();

    Integer getThreshold();
}
