package ut.fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import org.junit.Test;

import static org.junit.Assert.*;

public class AlreadyDecidedBattlesShould {

    private static final String USERKEY = "USERKEY";
    private AlreadyDecidedBattles alreadyDecidedBattles;

    @Test
    public void ask_for_repository_if_battle_is_decided() throws Exception {
        alreadyDecidedBattles = new AlreadyDecidedBattles();

        alreadyDecidedBattles.forIdentifier( USERKEY );
    }
}