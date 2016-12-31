package fi.ambientia.atlassian.routes.feature_battles;

import fi.ambientia.abtesting.action.ChooseAWinnerOfAFeatureBattle;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.routes.arguments.FeatureBattleWinCommand;
import org.junit.Before;
import org.junit.Test;
import ut.fi.ambientia.abtesting.model.TestData;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ut.fi.ambientia.mocks.Dummy.dummy;

public class FeatureBattleWinsShould {

    public static final FeatureBattleIdentifier FEATURE_BATTLE_IDENTIFIER = TestData.FEATURE_BATTLE_IDENTIFIER;
    public static final UserIdentifier USER_IDENTIFIER = TestData.USERIDENTIFIER;
    public static final String USER_KEY = USER_IDENTIFIER.getIdentifier();
    private FeatureBattleWins featureBattleWins;
    private FeatureBattleRoute featureBattleRoute;
    private HttpServletRequest httpServletRequest;
    private FeatureBattleWinCommand featureBattleWinCommand;
    private ChooseAWinnerOfAFeatureBattle chooseAWinnerOfAFeatureBattle;

    @Before
    public void setUp() throws Exception {
        featureBattleRoute = mock(FeatureBattleRoute.class);
        chooseAWinnerOfAFeatureBattle = mock(ChooseAWinnerOfAFeatureBattle.class);

        httpServletRequest = dummy(HttpServletRequest.class);
        when(featureBattleRoute.head(httpServletRequest, FEATURE_BATTLE_IDENTIFIER.getIdentifier())).thenReturn( Response.status(200).build() );

        featureBattleWinCommand = new FeatureBattleWinCommand(Experiment.Type.GOOD_OLD, USER_KEY);
        featureBattleWins = new FeatureBattleWins( featureBattleRoute, chooseAWinnerOfAFeatureBattle);
    }

    @Test
    public void return_404_if_feature_battle_not_found() throws Exception {
        when(featureBattleRoute.head(httpServletRequest, FEATURE_BATTLE_IDENTIFIER.getIdentifier())).thenReturn( Response.status(404).build() );

        Response response = featureBattleWins.createNew(httpServletRequest, FEATURE_BATTLE_IDENTIFIER.getIdentifier(), featureBattleWinCommand);

        assertThat( response.getStatus(), equalTo(404));
    }

    @Test
    public void call_action_to_create_feature_battle_win() throws Exception {

        Consumer<Experiment.Type> mockConsumer = mock(Consumer.class);
        when(chooseAWinnerOfAFeatureBattle.forFeatureBattle( USER_IDENTIFIER, FEATURE_BATTLE_IDENTIFIER)).thenReturn( mockConsumer );

        Response response = featureBattleWins.createNew(httpServletRequest, FEATURE_BATTLE_IDENTIFIER.getIdentifier(), featureBattleWinCommand);

        assertThat(response.getStatus(), equalTo(200));
        verify( mockConsumer).accept( Experiment.Type.GOOD_OLD );
    }
}