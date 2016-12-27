package fi.ambientia.abtesting.model;

import fi.ambientia.abtesting.model.experiments.FeatureBattleIdentifier;

public class FeatureBattle {

    private final FeatureBattleIdentifier featureBattleIdentifier;

    public FeatureBattle(FeatureBattleIdentifier featureBattleIdentifier) {
        this.featureBattleIdentifier = featureBattleIdentifier;
    }

    public FeatureBattleIdentifier getIdentifier() {
        return featureBattleIdentifier;
    }
}
