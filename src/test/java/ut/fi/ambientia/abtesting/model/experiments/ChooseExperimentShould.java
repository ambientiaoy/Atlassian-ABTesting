package ut.fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseExperiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.ExecuteFeatureBattle;
import fi.ambientia.abtesting.action.experiments.feature_battles.RandomizeFeatureBattle;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ut.fi.ambientia.abtesting.model.TestData;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ChooseExperimentShould {

    public static final FeatureBattleIdentifier FEATURE_BATTLE_IDENTIFIER = TestData.FEATURE_BATTLE_IDENTIFIER;
    private ChooseExperiment chooseFeature;
    private AlreadyDecidedBattles alreadyDecidedBattles;
    private ExecuteFeatureBattle executeFeatureBattle;
    private RandomizeFeatureBattle randomizeFeatureBattle;
    private final UserIdentifier USER_IDENTIFIER = TestData.USERIDENTIFIER;

    @Before
    public void setUp() throws Exception {
        alreadyDecidedBattles = mock(AlreadyDecidedBattles.class);
        executeFeatureBattle = mock(ExecuteFeatureBattle.class);
        randomizeFeatureBattle = mock(RandomizeFeatureBattle.class);
        chooseFeature = new ChooseExperiment(alreadyDecidedBattles, executeFeatureBattle, randomizeFeatureBattle);
        when(executeFeatureBattle.forExperiment( any() )).thenReturn((u) -> Optional.of(TestData.getNewAndShiny()));
        when(randomizeFeatureBattle.getExperiment(FEATURE_BATTLE_IDENTIFIER)).thenReturn((u) -> {
            assertThat( u, equalTo( TestData.USERIDENTIFIER ));
            return null;
        });

    }

    @Test
    public void return_the_already_decided_feature_battle_if_it_is_decided() throws Exception {

        when(alreadyDecidedBattles.experimentOf(FEATURE_BATTLE_IDENTIFIER, USER_IDENTIFIER)).thenReturn((user) -> Optional.of(TestData.getNewAndShiny()));

        Experiment experiment = chooseFeature.forFeatureBattle(USER_IDENTIFIER, FEATURE_BATTLE_IDENTIFIER).matching( ChooseExperiment.forUser( TestData.USERIDENTIFIER ));

        assertThat(experiment.type(), equalTo(Experiment.Type.NEW_AND_SHINY));
    }

    @Test
    public void execute_a_new_battle_for_user_that_does_not_have_already_decided_battle() throws Exception {
        when(alreadyDecidedBattles.experimentOf(FEATURE_BATTLE_IDENTIFIER, USER_IDENTIFIER)).thenReturn( (u) -> Optional.empty() );

        Experiment experiment = chooseFeature.forFeatureBattle(USER_IDENTIFIER, FEATURE_BATTLE_IDENTIFIER).matching(ChooseExperiment.forUser( TestData.USERIDENTIFIER ) );

        verify( executeFeatureBattle ).forExperiment(FEATURE_BATTLE_IDENTIFIER);
    }

    @Test
    public void execute_a_randomizer_to_call_random_page() throws Exception {
        when(alreadyDecidedBattles.experimentOf(FEATURE_BATTLE_IDENTIFIER, USER_IDENTIFIER)).thenReturn( (u) -> Optional.empty() );
        when(randomizeFeatureBattle.getExperiment(FEATURE_BATTLE_IDENTIFIER)).thenReturn(u -> TestData.getGoodOld() );

        Experiment experiment = chooseFeature.forFeatureBattle(USER_IDENTIFIER, FEATURE_BATTLE_IDENTIFIER).matching( ChooseExperiment.forUser( TestData.USERIDENTIFIER ) );

        assertThat(experiment.type(), equalTo(Experiment.Type.GOOD_OLD));
    }

    @Ignore
    @Test
    public void find_feature_battle_result_for_experiment_type(){

    }

}