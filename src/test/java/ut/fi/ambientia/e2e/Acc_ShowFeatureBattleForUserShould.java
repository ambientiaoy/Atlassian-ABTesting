package ut.fi.ambientia.e2e;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.confluence.macro.MacroExecutionException;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseExperiment;
import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleResultAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.atlassian.macro.experiments.DisplayFeatureBattle;
import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;
import fi.ambientia.atlassian.routes.experiments.FeatureBattleRoute;
import fi.ambientia.atlassian.routes.experiments.FeatureBattles;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import fi.ambientia.abtesting.infrastructure.WrappingActiveObjects;
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
public class Acc_ShowFeatureBattleForUserShould {


    public static final int CONSTANT_BIG_ENOUGH_TO_HAVE_NEW_AND_SHINY = 200;
    private static final int SMALL_ENOUGH_FOR_GOOD_OLD = -1;
    private final Bootstrap bootstrap = new Bootstrap();
    private EntityManager entityManager;
    private ActiveObjects ao;
    private TestPluginProperties properties;
    private ChooseExperiment chooseExperiment;
    private FeatureBattleRoute featureBattleRoute;
    private FeatureBattles featureBattles;
    private DisplayFeatureBattle displayFeatureBattle;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);

        properties = new TestPluginProperties();
        properties.setProperty("default.abtest.space.key", "FOOBAR");

        ao = new TestActiveObjects(entityManager);
        ao.migrate(ExperimentAO.class);
        ao.migrate(FeatureBattleAO.class);
        ao.migrate(FeatureBattleResultAO.class);
        ao.migrate(UserExperimentAO.class);
        SimpleActiveObjects sao = new WrappingActiveObjects(ao);


        bootstrap.bootstrap( sao );

        properties = bootstrap.getProperties();
        chooseExperiment = bootstrap.getChooseExperiment();
        featureBattleRoute = bootstrap.getFeatureBattleRoute();
        featureBattles = bootstrap.getFeatureBattles();
        displayFeatureBattle = bootstrap.getDisplayBattle();

    }

    @Test
    public void by_default_user_will_get_a_feature_battle_result_that_is_defined_when_feature_battle_is_created(){
        properties.setProperty("default.abtest.space.key", "FOOBAR");
        properties.setProperty("feature.battle.default.win", CONSTANT_BIG_ENOUGH_TO_HAVE_NEW_AND_SHINY);

        CreateNewFeatureBattleCommand newAbTest = new CreateNewFeatureBattleCommand( TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier(), SMALL_ENOUGH_FOR_GOOD_OLD, "Good Old", "Shiny new");
        featureBattles.createNew(dummy( HttpServletRequest.class), newAbTest);

        Experiment experiment = chooseExperiment.forFeatureBattle( TestData.USERIDENTIFIER, TestData.FEATURE_BATTLE_IDENTIFIER).matching( ChooseExperiment.forUser( TestData.USERIDENTIFIER ));

        assertThat( experiment.type(), equalTo(Experiment.Type.GOOD_OLD));
        assertThat( experiment.render(), equalTo(String.format(Experiment.INCLUDE_PAGE, "FOOBAR", "Good Old")));
    }

    @Test
    public void by_default_user_will_get_a_feature_battle_result_that_is_defined_when_feature_battle_is_created_as_shown_on_macro() throws MacroExecutionException {
        CreateNewFeatureBattleCommand newAbTest = new CreateNewFeatureBattleCommand( TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier(), SMALL_ENOUGH_FOR_GOOD_OLD, "Good Old", "Shiny new");
        featureBattles.createNew(dummy( HttpServletRequest.class), newAbTest);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("feature_battle", TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier());

        String execute = displayFeatureBattle.execute(parameters, "", null);

        assertThat( execute, equalTo( String.format( Experiment.INCLUDE_PAGE, "FOOBAR", "Good Old") ) );
    }

    @Test
    public void in_macro_user_can_choose_winner_by_defining_action_parameter_to() throws Exception {
        //arrange - create a featurebattle that would always  return GoodOld
        properties.setProperty("default.abtest.space.key", "FOOBAR");
        CreateNewFeatureBattleCommand newAbTest = new CreateNewFeatureBattleCommand( TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier(), SMALL_ENOUGH_FOR_GOOD_OLD, "Good Old", "Shiny new");
        featureBattles.createNew(dummy( HttpServletRequest.class), newAbTest);

        // fake the parameter.
        when(bootstrap.httpServletRequestMock.getParameter("featureBattleWinner")).thenReturn("new_and_shiny");
        // act - call for the featurebattle
        Map<String, String> parameters = new HashMap<>();
        parameters.put("feature_battle", TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier());
        String execute = displayFeatureBattle.execute(parameters, "", null);

        assertThat( execute, equalTo( String.format( Experiment.INCLUDE_PAGE, "FOOBAR", "Shiny new") ) );
    }


}
