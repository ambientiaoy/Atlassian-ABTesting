package fi.ambientia.abtesting.model.experiments;

public class GoodOldWay implements Experiment {
    @Override
    public Type type() {
        return Type.GOOD_OLD;
    }
}
