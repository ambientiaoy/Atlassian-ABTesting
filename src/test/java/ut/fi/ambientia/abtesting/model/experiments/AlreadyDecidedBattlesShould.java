package ut.fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseExperiment;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import fi.ambientia.abtesting.model.feature_battles.UserExperimentRepository;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ut.fi.ambientia.abtesting.model.TestData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AlreadyDecidedBattlesShould {

    private static final String USERKEY = "USERKEY";
    private static final UserIdentifier USER_IDENTIFIER = new UserIdentifier(USERKEY);
    private static final FeatureBattleIdentifier EXPERIMENT_IDENTIFIER = new FeatureBattleIdentifier("EXPERIMENT");
    private AlreadyDecidedBattles alreadyDecidedBattles;
    private FeatureBattleResults featureBattleRepository;
    private final GoodOldWay goodOldWay = TestData.getGoodOld();
    private UserExperimentRepository userExperimentRepository;

    @Before
    public void setUp() throws Exception {
        featureBattleRepository = mock(FeatureBattleResults.class);
        userExperimentRepository = mock(UserExperimentRepository.class);
        alreadyDecidedBattles = new AlreadyDecidedBattles( featureBattleRepository, userExperimentRepository );
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


    @Test
    public void search_for_UserExperiments_for_user_if_user_experiment_has_been_set() throws Exception {
        // user has not set a clear winner
        when(featureBattleRepository.featureBattleResultsFor( EXPERIMENT_IDENTIFIER )).thenReturn(new ArrayList() );
        // when user has experiments set
        when(userExperimentRepository.featureBattleResultsFor( EXPERIMENT_IDENTIFIER )).thenReturn( new ArrayList<>() ) ;
        Optional<Experiment> optional = alreadyDecidedBattles.experimentOf( EXPERIMENT_IDENTIFIER ).targetedFor( ChooseExperiment.forUser( USER_IDENTIFIER ) );

        optional.ifPresent( experiment -> fail("Should not have any, should be empty optional"));
        verify(userExperimentRepository).featureBattleResultsFor( EXPERIMENT_IDENTIFIER );
    }

    @Test
    public void search_for_return_userExperiments_for_user_that_is_set() throws Exception {
        // user has not set a clear winner
        when(featureBattleRepository.featureBattleResultsFor( EXPERIMENT_IDENTIFIER )).thenReturn(new ArrayList() );
        // when user has experiments set
        when(userExperimentRepository.featureBattleResultsFor( EXPERIMENT_IDENTIFIER )).thenReturn( Arrays.asList(createTestFeatureBattleResult(goodOldWay)  ) ) ;
        Optional<Experiment> optional = alreadyDecidedBattles.experimentOf( EXPERIMENT_IDENTIFIER ).targetedFor( ChooseExperiment.forUser( USER_IDENTIFIER ) );

        assertThat(optional.get(), equalTo( goodOldWay ));
    }

    private FeatureBattleResult createTestFeatureBattleResult(GoodOldWay goodOldWay) {
        return new FeatureBattleResult( USER_IDENTIFIER, goodOldWay );
    }
}