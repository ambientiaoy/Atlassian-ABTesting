package fi.ambientia.abtesting.model.experiments;

public interface Experiment {

    String ABTEST = "ABTEST";
    String INCLUDE_PAGE = "{include:%s:%s}";

    Type type();
    String render();

    enum Type {
        NEW_AND_SHINY,
        GOOD_OLD
    }
}
