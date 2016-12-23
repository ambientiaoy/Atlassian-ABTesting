package ut.fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.FeatureBattleRepository;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlreadyDecidedBattlesShould {

    private static final String USERKEY = "USERKEY";
    private static final UserIdentifier USER_IDENTIFIER = new UserIdentifier(USERKEY);
    private static final ExperimentIdentifier EXPERIMENT_IDENTIFIER = new ExperimentIdentifier("EXPERIMENT");
    private AlreadyDecidedBattles alreadyDecidedBattles;
    private FeatureBattleRepository featureBattleRepository;

    @Before
    public void setUp() throws Exception {
        featureBattleRepository = mock(FeatureBattleRepository.class);
        alreadyDecidedBattles = new AlreadyDecidedBattles( featureBattleRepository );
    }

    @Test
    public void get_list_of_all_feature_battles_for_given_experiment() throws Exception {
        GoodOldWay goodOldWay = new GoodOldWay(EXPERIMENT_IDENTIFIER);
        when(featureBattleRepository.experimentsFor( EXPERIMENT_IDENTIFIER )).
                thenReturn(
                        Arrays.asList(createTestFeatureBattleResult(goodOldWay)
                        ) );

        Optional<Experiment> optional = alreadyDecidedBattles.experimentOf( EXPERIMENT_IDENTIFIER ).targetedFor( USER_IDENTIFIER );

        assertThat(optional.get(), equalTo( goodOldWay ));
    }

//    @Test
//    public void see_if_user_has_already_decided_to_take_either() throws Exception {
//        GoodOldWay goodOldWay = new GoodOldWay();
//        when(featureBattleRepository.votesFor( EXPERIMENT_IDENTIFIER )).
//                thenReturn(
//                        Arrays.asList(createTestFeatureBattleResultWhereUserWantsOldWay(goodOldWay)
//                        ) );
//
//        Optional<Experiment> optional = alreadyDecidedBattles.forExperiment( EXPERIMENT_IDENTIFIER ).targetedFor( USER_IDENTIFIER );
//
//        assertThat(optional.get(), equalTo( goodOldWay ));
//
//
//    }

    private FeatureBattleResult createTestFeatureBattleResultWhereUserWantsOldWay(GoodOldWay goodOldWay) {
        return null;
    }

    private FeatureBattleResult createTestFeatureBattleResult(GoodOldWay goodOldWay) {
        return new FeatureBattleResult( USER_IDENTIFIER, goodOldWay );
    }
}