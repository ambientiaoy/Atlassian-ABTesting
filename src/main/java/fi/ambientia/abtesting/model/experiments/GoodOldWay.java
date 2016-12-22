package fi.ambientia.abtesting.model.experiments;

public class GoodOldWay implements Experiment {

    public static final String B_1_GOOD_OLD = "b1_good_old";

    @Override
    public Type type() {
        return Type.GOOD_OLD;
    }

    @Override
    public String render() {
        return String.format(INCLUDE_PAGE, ABTEST, B_1_GOOD_OLD);
    }
}
