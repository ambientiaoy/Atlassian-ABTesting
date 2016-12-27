package fi.ambientia.abtesting.model.experiments;

public class NewAndShiny implements Experiment {

    private static final String NEW_AND_SHINY = "b2_new_and_shiny";
    private final FeatureBattleIdentifier experientIdenifier;

    public NewAndShiny(FeatureBattleIdentifier experientIdenifier) {
        this.experientIdenifier = experientIdenifier;
    }

    @Override
    public String toString() {
        return "NewAndShiny{" +
                "experientIdenifier=" + experientIdenifier.getIdentifier() +
                '}';
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
    public boolean isRepresentedBy(FeatureBattleIdentifier identifier) {
        return identifier != null && identifier.equals(experientIdenifier);
    }
}
