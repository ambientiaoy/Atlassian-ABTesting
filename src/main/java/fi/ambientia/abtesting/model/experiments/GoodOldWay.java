package fi.ambientia.abtesting.model.experiments;

public class GoodOldWay implements Experiment {

    public static final String B_1_GOOD_OLD = "b1_good_old";
    private final FeatureBattleIdentifier featureBattleIdentifier;

    public GoodOldWay(FeatureBattleIdentifier featureBattleIdentifier) {
        this.featureBattleIdentifier = featureBattleIdentifier;
    }

    @Override
    public Type type() {
        return Type.GOOD_OLD;
    }

    @Override
    public String render() {
        return String.format(INCLUDE_PAGE, ABTEST, B_1_GOOD_OLD);
    }

    @Override
    public boolean isRepresentedBy(FeatureBattleIdentifier featureBattleIdentifier) {
        return this.featureBattleIdentifier != null && this.featureBattleIdentifier.equals(featureBattleIdentifier) ;
    }
}
