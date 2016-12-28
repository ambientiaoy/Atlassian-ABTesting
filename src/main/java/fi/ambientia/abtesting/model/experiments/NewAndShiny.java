package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;

public class NewAndShiny implements Experiment {

    private static final String NEW_AND_SHINY = "b2_new_and_shiny";
    private final FeatureBattleIdentifier experientIdenifier;
    private final String page;

    public NewAndShiny(FeatureBattleIdentifier experientIdenifier, String page) {
        this.experientIdenifier = experientIdenifier;
        this.page = page;
    }

    @Override
    public String toString() {
        return "NewAndShiny{" +
                "experientIdentifier=" + experientIdenifier.getIdentifier() +
                '}';
    }

    @Override
    public Type type() {
        return Type.NEW_AND_SHINY;
    }

    @Override
    public String render() {
        return String.format(INCLUDE_PAGE, ABTEST, page);
    }

    @Override
    public boolean isRepresentedBy(FeatureBattleIdentifier identifier) {
        return identifier != null && identifier.equals(experientIdenifier);
    }
}
