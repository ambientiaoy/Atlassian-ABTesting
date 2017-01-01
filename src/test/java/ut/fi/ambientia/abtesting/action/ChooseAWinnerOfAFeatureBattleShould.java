package ut.fi.ambientia.abtesting.action;

import fi.ambientia.abtesting.action.ChooseAWinnerOfAFeatureBattle;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.junit.Before;
import org.junit.Test;
import ut.fi.ambientia.abtesting.model.TestData;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChooseAWinnerOfAFeatureBattleShould {

    public static final UserIdentifier USERIDENTIFIER = TestData.USERIDENTIFIER;
    public static final FeatureBattleIdentifier FEATURE_BATTLE_IDENTIFIER = TestData.FEATURE_BATTLE_IDENTIFIER;
    private ChooseAWinnerOfAFeatureBattle chooseAWinnerOfAFeatureBattle;
    private FeatureBattleResults featureBattleResults;
    private FeatureBattleRepository featureBattleRepository;

    @Before
    public void setUp() throws Exception {
        featureBattleResults = mock(FeatureBattleResults.class);
        featureBattleRepository = mock(FeatureBattleRepository.class);

        when(featureBattleRepository.ensureExistsOnlyOne( FEATURE_BATTLE_IDENTIFIER)).thenReturn(Optional.of( FEATURE_BATTLE_IDENTIFIER ));
        chooseAWinnerOfAFeatureBattle = new ChooseAWinnerOfAFeatureBattle( featureBattleResults, featureBattleRepository);
    }

    @Test
    public void store_feature_battle_winner_for_user() throws Exception {

        FeatureBattleResults.AndStoreResult mockObject = mock(FeatureBattleResults.AndStoreResult.class);
        when( featureBattleResults.newWinnerFor(FEATURE_BATTLE_IDENTIFIER)).thenReturn( (userIdentifier) -> mockObject );
        chooseAWinnerOfAFeatureBattle.forFeatureBattle(USERIDENTIFIER, FEATURE_BATTLE_IDENTIFIER, Experiment.Type.GOOD_OLD);

//        verify(mockObject).newWinnerFor(FEATURE_BATTLE_IDENTIFIER).forUser(USERIDENTIFIER).resultBeing( Experiment.Type.GOOD_OLD );
        verify(mockObject).resultBeing( Experiment.Type.GOOD_OLD );
    }
}