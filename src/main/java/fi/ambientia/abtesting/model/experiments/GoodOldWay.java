package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;

public class GoodOldWay implements Experiment {

    public static final String B_1_GOOD_OLD = "b1_good_old";
    private final FeatureBattleIdentifier featureBattleIdentifier;
    private final String page;

    public GoodOldWay(FeatureBattleIdentifier featureBattleIdentifier, String page) {
        this.featureBattleIdentifier = featureBattleIdentifier;
        this.page = page;
    }

    @Override
    public Type type() {
        return Type.GOOD_OLD;
    }

    @Override
    public String render() {
        return String.format(INCLUDE_PAGE, ABTEST, page);
    }

    @Override
    public boolean isRepresentedBy(FeatureBattleIdentifier featureBattleIdentifier) {
        return this.featureBattleIdentifier != null && this.featureBattleIdentifier.equals(featureBattleIdentifier) ;
    }
}
