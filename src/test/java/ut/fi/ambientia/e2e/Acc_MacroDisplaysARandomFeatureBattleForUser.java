package ut.fi.ambientia.e2e;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import fi.ambientia.abtesting.infrastructure.WrappingActiveObjects;
import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleResultAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.atlassian.macro.experiments.DisplayFeatureBattle;
import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;
import fi.ambientia.atlassian.routes.arguments.UpdateFeatureBattleThresholdCommand;
import fi.ambientia.atlassian.routes.feature_battles.FeatureBattleThresholdRoute;
import fi.ambientia.atlassian.routes.feature_battles.FeatureBattles;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import ut.fi.ambientia.abtesting.model.TestData;
import ut.fi.ambientia.e2e.bootstrap.Bootstrap;
import ut.fi.ambientia.helpers.TestPluginProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ut.fi.ambientia.mocks.Dummy.dummy;

@RunWith(ActiveObjectsJUnitRunner.class)
public class Acc_MacroDisplaysARandomFeatureBattleForUser {

    public static final int CONSTANT_BIG_ENOUGH_TO_HAVE_NEW_AND_SHINY = 200;
    private static final int SMALL_ENOUGH_FOR_GOOD_OLD = -1;
    private EntityManager entityManager;
    private ActiveObjects ao;
    private TestPluginProperties properties;
    private FeatureBattles featureBattles;
    private DisplayFeatureBattle displayFeatureBattle;
    private Bootstrap bootstrap;
    private Map<String, String> parameters;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        ao.migrate(ExperimentAO.class);
        ao.migrate(FeatureBattleAO.class);
        ao.migrate(FeatureBattleResultAO.class);
        ao.migrate(UserExperimentAO.class);
        SimpleActiveObjects sao = new WrappingActiveObjects(ao);

        UserManager userManager = mock(UserManager.class);
        when(userManager.getRemoteUserKey()).thenReturn( new UserKey("ANY USER"));

        bootstrap = new Bootstrap();
        bootstrap.bootstrap( sao );

        properties = bootstrap.getProperties();
        featureBattles = bootstrap.getFeatureBattles();
        displayFeatureBattle = bootstrap.getDisplayBattle();
        FeatureBattleThresholdRoute updateFeatureBattleThreshold = bootstrap.getFeatureBattleThresholdRoute();

        // act - call for the featurebattle
        parameters = new HashMap<>();
        parameters.put("feature_battle", TestData.FEATURE_BATTLE_IDENTIFIER.getFeatureBattleId());

    }

    @Test
    public void which_is_GOOD_OLD_given_very_low_threshold() throws MacroExecutionException {
        properties.setProperty("default.abtest.space.key", "TEST");

        createNewFeatureBattleWithThresholdOf(SMALL_ENOUGH_FOR_GOOD_OLD);

        String execute = displayFeatureBattle.execute(parameters, "", null);

        assertThat( execute, equalTo( String.format( Experiment.INCLUDE_PAGE, "TEST", "Good Old") ) );
    }


    @Test
    public void displays_always_the_same_random_after_the_first_time_a_featureBattle_is_randomized() throws Exception {
        //arrange - create a featurebattle that would always  return GoodOld
        properties.setProperty("default.abtest.space.key", "TEST");

        createNewFeatureBattleWithThresholdOf(50);

        String one_created_for_user = displayFeatureBattle.execute(parameters, "", null);

        int iterations = 10;
        for (int i = 1; i <= iterations; i++) {
            String execute = displayFeatureBattle.execute(parameters, "", null);
            assertThat("on " + i + "th iteration" , execute, equalTo( one_created_for_user ) );
        }
    }

    @Test
    public void which_is_reRandomized_for_users_with_GOOD_OLD_once_the_FeatureBattle_threshold_is_updated() throws MacroExecutionException {
        properties.setProperty("default.abtest.space.key", "TEST");

        createNewFeatureBattleWithThresholdOf(SMALL_ENOUGH_FOR_GOOD_OLD);

        String execute = displayFeatureBattle.execute(parameters, "", null);
        assertThat( execute, equalTo( String.format( Experiment.INCLUDE_PAGE, "TEST", "Good Old") ) );

        updateFeatureBattleWithThresholdOf(CONSTANT_BIG_ENOUGH_TO_HAVE_NEW_AND_SHINY);
        String should_be_new_and_shiny = displayFeatureBattle.execute(parameters, "", null);
        assertThat( should_be_new_and_shiny, equalTo( String.format( Experiment.INCLUDE_PAGE, "TEST", "New and shiny") ) );

    }
    @Test
    public void results_with_NEW_AND_SHINY_are_not_nulled_on_update() throws MacroExecutionException {
        properties.setProperty("default.abtest.space.key", "TEST");

        createNewFeatureBattleWithThresholdOf(CONSTANT_BIG_ENOUGH_TO_HAVE_NEW_AND_SHINY);

        String execute = displayFeatureBattle.execute(parameters, "", null);
        assertThat( execute, equalTo( String.format( Experiment.INCLUDE_PAGE, "TEST", "Good Old") ) );

        updateFeatureBattleWithThresholdOf(SMALL_ENOUGH_FOR_GOOD_OLD);
        String should_be_new_and_shiny = displayFeatureBattle.execute(parameters, "", null);
        assertThat( should_be_new_and_shiny, equalTo( String.format( Experiment.INCLUDE_PAGE, "TEST", "New and shiny") ) );

    }

    protected void createNewFeatureBattleWithThresholdOf(int threshold) {
        CreateNewFeatureBattleCommand newAbTest = new CreateNewFeatureBattleCommand( TestData.FEATURE_BATTLE_IDENTIFIER.getFeatureBattleId(), threshold, "Good Old", "Shiny new");
        featureBattles.createNew(dummy( HttpServletRequest.class), newAbTest);
    }

    private void updateFeatureBattleWithThresholdOf(int threshold) {
        UpdateFeatureBattleThresholdCommand newAbTest = new UpdateFeatureBattleThresholdCommand( TestData.FEATURE_BATTLE_IDENTIFIER.getFeatureBattleId(), threshold);
        bootstrap.getFeatureBattleThresholdRoute().update(dummy( HttpServletRequest.class), newAbTest);
    }


    @Ignore
    @Test
    public void cannot_create_experiment_if_page_is_not_found() throws Exception {
        fail("ToBeDefined");
    }

    @Ignore
    @Test
    public void can_create_experiment_and_is_shown_in_rest_api() throws Exception {
        fail("ToBeDefined");
    }

    @Ignore
    @Test
    public void can_update_experiment() throws Exception {
        fail("ToBeDefined");
    }

    @Ignore
    @Test
    public void can_mark_experiment_as_done(){
        // not Delete, mark as done, choose either experiment.
        fail("ToBeDefined");
    }

}
