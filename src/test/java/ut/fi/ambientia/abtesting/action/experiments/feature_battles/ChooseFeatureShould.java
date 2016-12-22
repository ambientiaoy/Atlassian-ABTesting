package ut.fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseFeature;
import fi.ambientia.abtesting.action.experiments.feature_battles.ExecuteFeatureBattle;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChooseFeatureShould {

    private static final String USERKEY = "USER KEY";
    private ChooseFeature chooseFeature;
    private AlreadyDecidedBattles alreadyDecidedBattles;
    private ExecuteFeatureBattle executeFeatureBattle;

    @Before
    public void setUp() throws Exception {
        alreadyDecidedBattles = mock(AlreadyDecidedBattles.class);
        executeFeatureBattle = mock(ExecuteFeatureBattle.class);
        chooseFeature = new ChooseFeature(alreadyDecidedBattles, executeFeatureBattle);
    }

    @Test
    public void return_the_already_decided_feature_battle_if_it_is_decided() throws Exception {
        when(alreadyDecidedBattles.forIdentifier( USERKEY )).thenReturn(Optional.of( new NewAndShiny() ) );

        Experiment experiment = chooseFeature.forUser(USERKEY);

        assertThat(experiment.type(), equalTo(Experiment.Type.NEW_AND_SHINY));
    }

    @Test
    public void execute_a_new_battle_for_user_that_does_not_have_already_decided_battle() throws Exception {
        when(alreadyDecidedBattles.forIdentifier( USERKEY )).thenReturn( Optional.empty(), Optional.of( new GoodOldWay() ) );

        Experiment experiment = chooseFeature.forUser(USERKEY);

        verify( executeFeatureBattle ).forIdentifier( USERKEY );
        assertThat(experiment.type(), equalTo(Experiment.Type.GOOD_OLD));
    }
}