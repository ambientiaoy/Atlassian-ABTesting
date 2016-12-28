package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;

public class MissingExperiment implements Experiment {
    @Override
    public Type type() {
        return Type.GOOD_OLD;
    }

    @Override
    public String render() {
        return "";
    }

    @Override
    public boolean isRepresentedBy(FeatureBattleIdentifier featureBattleIdentifier) {
        return false;
    }
}
