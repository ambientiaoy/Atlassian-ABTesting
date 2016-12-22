package ut.fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.model.experiments.FeatureBattleRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class AlreadyDecidedBattlesShould {

    private static final String USERKEY = "USERKEY";
    private AlreadyDecidedBattles alreadyDecidedBattles;

    @Before
    public void setUp() throws Exception {
        FeatureBattleRepository featureBattleRepository = mock(FeatureBattleRepository.class);
    }

    @Test
    public void ask_for_repository_if_battle_is_decided() throws Exception {
        alreadyDecidedBattles = new AlreadyDecidedBattles();

        alreadyDecidedBattles.forIdentifier( USERKEY );
    }
}