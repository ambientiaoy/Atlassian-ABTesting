package ut.fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseExperiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.ExecuteFeatureBattle;
import fi.ambientia.abtesting.action.experiments.feature_battles.RandomizeFeatureBattle;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChooseExperimentShould {

    private static final String USERKEY = "USER KEY";
    public static final UserIdentifier USERIDENTIFIER = new UserIdentifier(USERKEY);
    public static final ExperimentIdentifier EXPERIMENT_IDENTIFIER = new ExperimentIdentifier("EXPERIMENT_IDENTIFIER");
    private ChooseExperiment chooseFeature;
    private AlreadyDecidedBattles alreadyDecidedBattles;
    private ExecuteFeatureBattle executeFeatureBattle;
    private RandomizeFeatureBattle randomizeFeatureBattle;

    @Before
    public void setUp() throws Exception {
        alreadyDecidedBattles = mock(AlreadyDecidedBattles.class);
        executeFeatureBattle = mock(ExecuteFeatureBattle.class);
        randomizeFeatureBattle = mock(RandomizeFeatureBattle.class);
        chooseFeature = new ChooseExperiment(alreadyDecidedBattles, executeFeatureBattle, randomizeFeatureBattle);
        when(executeFeatureBattle.forExperiment( any() )).thenReturn( (u) -> Optional.of(getANewAndShiny()));
        when(randomizeFeatureBattle.getExperiment( EXPERIMENT_IDENTIFIER)).thenReturn((u) -> {
            assertThat( u, equalTo( USERIDENTIFIER ));
            return null;
        });

    }

    @Test
    public void return_the_already_decided_feature_battle_if_it_is_decided() throws Exception {

        when(alreadyDecidedBattles.experimentOf( EXPERIMENT_IDENTIFIER)).thenReturn( (user) -> Optional.of(getANewAndShiny()) );

        Experiment experiment = chooseFeature.forUser(USERIDENTIFIER, EXPERIMENT_IDENTIFIER);

        assertThat(experiment.type(), equalTo(Experiment.Type.NEW_AND_SHINY));
    }

    @Test
    public void execute_a_new_battle_for_user_that_does_not_have_already_decided_battle() throws Exception {
        when(alreadyDecidedBattles.experimentOf( EXPERIMENT_IDENTIFIER )).thenReturn( (u) -> Optional.empty() );

        Experiment experiment = chooseFeature.forUser(USERIDENTIFIER, EXPERIMENT_IDENTIFIER);

        verify( executeFeatureBattle ).forExperiment(EXPERIMENT_IDENTIFIER);
    }

    @Test
    public void execute_a_randomizer_to_call_random_page() throws Exception {
        when(alreadyDecidedBattles.experimentOf( EXPERIMENT_IDENTIFIER )).thenReturn( (u) -> Optional.empty() );
        when(randomizeFeatureBattle.getExperiment( EXPERIMENT_IDENTIFIER)).thenReturn(u -> new GoodOldWay(EXPERIMENT_IDENTIFIER));

        Experiment experiment = chooseFeature.forUser(USERIDENTIFIER, EXPERIMENT_IDENTIFIER);

        assertThat(experiment.type(), equalTo(Experiment.Type.GOOD_OLD));
    }

    private NewAndShiny getANewAndShiny() {
        return new NewAndShiny(EXPERIMENT_IDENTIFIER);
    }
}