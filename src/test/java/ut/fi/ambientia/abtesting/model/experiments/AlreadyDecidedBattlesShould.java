package ut.fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseExperiment;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ut.fi.ambientia.abtesting.model.TestData;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlreadyDecidedBattlesShould {

    private static final String USERKEY = "USERKEY";
    private static final UserIdentifier USER_IDENTIFIER = new UserIdentifier(USERKEY);
    private static final FeatureBattleIdentifier EXPERIMENT_IDENTIFIER = new FeatureBattleIdentifier("EXPERIMENT");
    private AlreadyDecidedBattles alreadyDecidedBattles;
    private FeatureBattleResults featureBattleRepository;
    private final GoodOldWay goodOldWay = TestData.getGoodOld();

    @Before
    public void setUp() throws Exception {
        featureBattleRepository = mock(FeatureBattleResults.class);
        alreadyDecidedBattles = new AlreadyDecidedBattles( featureBattleRepository );
        when(featureBattleRepository.featureBattleResultsFor( EXPERIMENT_IDENTIFIER )).
                thenReturn(
                        Arrays.asList(createTestFeatureBattleResult(goodOldWay)
                        ) );
    }

    @Test
    public void get_list_of_all_feature_battles_for_given_experiment() throws Exception {

        Optional<Experiment> optional = alreadyDecidedBattles.experimentOf( EXPERIMENT_IDENTIFIER ).targetedFor(ChooseExperiment.forUser( USER_IDENTIFIER ));

        assertThat(optional.get(), equalTo( goodOldWay ));
    }

    @Test
    public void not_receive_any_if_filter_is_set_to_return_false() throws Exception {
        Optional<Experiment> optional = alreadyDecidedBattles.experimentOf( EXPERIMENT_IDENTIFIER ).targetedFor( (any) -> false );
        optional.ifPresent( experiment -> fail("Should not have any, should be empty optional"));
    }

    private FeatureBattleResult createTestFeatureBattleResult(GoodOldWay goodOldWay) {
        return new FeatureBattleResult( USER_IDENTIFIER, goodOldWay );
    }
}