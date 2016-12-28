package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;

public class GoodOldWay implements Experiment {

    private final FeatureBattleIdentifier featureBattleIdentifier;
    private final PageObject pageObject;
    private final String page;

    public GoodOldWay(FeatureBattleIdentifier featureBattleIdentifier, PageObject pageObject) {
        this.featureBattleIdentifier = featureBattleIdentifier;
        this.pageObject = pageObject;
        this.page = pageObject.getPage();
    }

    @Override
    public Type type() {
        return Type.GOOD_OLD;
    }

    @Override
    public String render() {
        return String.format(INCLUDE_PAGE, pageObject.getSpaceKey(), page);
    }

    @Override
    public boolean isRepresentedBy(FeatureBattleIdentifier featureBattleIdentifier) {
        return this.featureBattleIdentifier != null && this.featureBattleIdentifier.equals(featureBattleIdentifier) ;
    }
}
