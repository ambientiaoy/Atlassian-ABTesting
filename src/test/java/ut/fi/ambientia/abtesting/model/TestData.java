package ut.fi.ambientia.abtesting.model;

import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;

public class TestData {
    public static final FeatureBattleIdentifier FEATURE_BATTLE_IDENTIFIER = new FeatureBattleIdentifier("FEATURE_BATTLE_IDENTIFIER");
    private static final String USERKEY = "USER KEY";
    public static final UserIdentifier USERIDENTIFIER = new UserIdentifier(USERKEY);

    public static NewAndShiny getNewAndShiny() {
        return new NewAndShiny(FEATURE_BATTLE_IDENTIFIER, "page");
    }

    public static GoodOldWay getGoodOld() {
        return new GoodOldWay(FEATURE_BATTLE_IDENTIFIER, "page");
    }
}
