package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;

public class NewAndShiny implements Experiment {

    private final FeatureBattleIdentifier experientIdenifier;
    private final PageObject pageObject;
    private final String page;

    public NewAndShiny(FeatureBattleIdentifier experientIdenifier, PageObject pageObject) {
        this.experientIdenifier = experientIdenifier;
        this.pageObject = pageObject;
        this.page = pageObject.getPage();
    }

    @Override
    public String toString() {
        return "NewAndShiny{" +
                "experientIdentifier=" + experientIdenifier.getFeatureBattleId() +
                '}';
    }

    @Override
    public Type type() {
        return Type.NEW_AND_SHINY;
    }

    @Override
    public String render() {
        return String.format(INCLUDE_PAGE, pageObject.getSpaceKey(), page);
    }

    @Override
    public boolean isRepresentedBy(FeatureBattleIdentifier identifier) {
        return identifier != null && identifier.equals(experientIdenifier);
    }

    @Override
    public String page() {
        return page;
    }
}
