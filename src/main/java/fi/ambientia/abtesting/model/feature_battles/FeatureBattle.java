package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;

public class FeatureBattle {

    private final FeatureBattleIdentifier featureBattleIdentifier;

    public FeatureBattle(FeatureBattleIdentifier featureBattleIdentifier) {
        this.featureBattleIdentifier = featureBattleIdentifier;
    }

    public FeatureBattleIdentifier getIdentifier() {
        return featureBattleIdentifier;
    }
}
