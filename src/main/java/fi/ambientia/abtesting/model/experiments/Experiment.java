package fi.ambientia.abtesting.model.experiments;

public interface Experiment {

    Type type();

    enum Type {
        NEW_AND_SHINY,
        GOOD_OLD
    }
}
