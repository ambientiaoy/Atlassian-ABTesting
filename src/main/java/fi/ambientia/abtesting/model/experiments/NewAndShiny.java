package fi.ambientia.abtesting.model.experiments;

public class NewAndShiny implements Experiment {

    private static final String NEW_AND_SHINY = "b2_new_and_shiny";
    private final ExperimentIdentifier experientIdenifier;

    public NewAndShiny(ExperimentIdentifier experientIdenifier) {
        this.experientIdenifier = experientIdenifier;
    }

    @Override
    public Type type() {
        return Type.NEW_AND_SHINY;
    }

    @Override
    public String render() {
        return String.format(INCLUDE_PAGE, ABTEST, NEW_AND_SHINY);
    }

    @Override
    public boolean isRepresentedBy(ExperimentIdentifier identifier) {
        return identifier != null && identifier.equals(experientIdenifier);
    }
}
