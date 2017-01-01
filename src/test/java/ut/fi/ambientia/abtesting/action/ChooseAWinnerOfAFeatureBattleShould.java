package ut.fi.ambientia.abtesting.action;

import fi.ambientia.abtesting.action.ChooseAWinnerOfAFeatureBattle;
import fi.ambientia.abtesting.events.ChooseAWinnerEvent;
import fi.ambientia.abtesting.model.EventLogger;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.junit.Before;
import org.junit.Test;
import ut.fi.ambientia.abtesting.model.TestData;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ChooseAWinnerOfAFeatureBattleShould {

    public static final UserIdentifier USERIDENTIFIER = TestData.USERIDENTIFIER;
    public static final FeatureBattleIdentifier FEATURE_BATTLE_IDENTIFIER = TestData.FEATURE_BATTLE_IDENTIFIER;
    private ChooseAWinnerOfAFeatureBattle chooseAWinnerOfAFeatureBattle;
    private FeatureBattleResults featureBattleResults;
    private FeatureBattleRepository featureBattleRepository;
    private EventLogger eventLogger;
    private EventLogger.LoggerOn failedOn;
    private EventLogger.LoggerOn succeededOn;

    @Before
    public void setUp() throws Exception {
        featureBattleResults = mock(FeatureBattleResults.class);
        featureBattleRepository = mock(FeatureBattleRepository.class);
        eventLogger = mock(EventLogger.class);
        failedOn = mock(EventLogger.LoggerOn.class);
        succeededOn = mock(EventLogger.LoggerOn.class);
        when(eventLogger.failed()).thenReturn(failedOn);
        when(eventLogger.succes()).thenReturn(succeededOn);

        when(featureBattleRepository.ensureExistsOnlyOne( FEATURE_BATTLE_IDENTIFIER)).thenReturn(Optional.of( FEATURE_BATTLE_IDENTIFIER ));
        chooseAWinnerOfAFeatureBattle = new ChooseAWinnerOfAFeatureBattle( featureBattleResults, featureBattleRepository, eventLogger);
    }

    @Test
    public void store_feature_battle_winner_for_user() throws Exception {

        FeatureBattleResults.AndStoreResult mockObject = mock(FeatureBattleResults.AndStoreResult.class);
        when( featureBattleResults.newWinnerFor(FEATURE_BATTLE_IDENTIFIER)).thenReturn( (userIdentifier) -> mockObject );
        chooseAWinnerOfAFeatureBattle.forFeatureBattle(new ChooseAWinnerEvent(USERIDENTIFIER, FEATURE_BATTLE_IDENTIFIER, Experiment.Type.GOOD_OLD));

        verify(mockObject).resultBeing( Experiment.Type.GOOD_OLD );
    }

    @Test
    public void log_event_on_success() throws Exception {

        FeatureBattleResults.AndStoreResult mockObject = mock(FeatureBattleResults.AndStoreResult.class);
        when( featureBattleResults.newWinnerFor(FEATURE_BATTLE_IDENTIFIER)).thenReturn( (userIdentifier) -> mockObject );
        ChooseAWinnerEvent chooseAWinnerEvent = new ChooseAWinnerEvent(USERIDENTIFIER, FEATURE_BATTLE_IDENTIFIER, Experiment.Type.GOOD_OLD);
        chooseAWinnerOfAFeatureBattle.forFeatureBattle(chooseAWinnerEvent);

        verify(succeededOn).on( chooseAWinnerEvent );
    }

    @Test
    public void should_log_error_if_feature_battle_entity_is_not_found() throws Exception {
        when(featureBattleRepository.ensureExistsOnlyOne( FEATURE_BATTLE_IDENTIFIER)).thenReturn(Optional.empty());


        ChooseAWinnerEvent chooseAWinnerEvent = new ChooseAWinnerEvent(USERIDENTIFIER, FEATURE_BATTLE_IDENTIFIER, Experiment.Type.GOOD_OLD);
        chooseAWinnerOfAFeatureBattle.forFeatureBattle(chooseAWinnerEvent);

        verify( failedOn ).on( chooseAWinnerEvent  );
    }
}