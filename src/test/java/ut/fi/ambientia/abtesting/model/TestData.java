package ut.fi.ambientia.abtesting.model;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;

public class TestData {
    public static final FeatureBattleIdentifier FEATURE_BATTLE_IDENTIFIER = new FeatureBattleIdentifier("FEATURE_BATTLE_IDENTIFIER");
    private static final String USERKEY = "USER KEY";
    public static final UserIdentifier USERIDENTIFIER = new UserIdentifier(USERKEY);
}
