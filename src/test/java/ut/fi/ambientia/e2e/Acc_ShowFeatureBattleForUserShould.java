package ut.fi.ambientia.e2e;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.confluence.macro.MacroExecutionException;
import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleResultAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.macro.experiments.DisplayFeatureBattle;
import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;
import fi.ambientia.atlassian.routes.arguments.FeatureBattleWinCommand;
import fi.ambientia.atlassian.routes.feature_battles.FeatureBattleRoute;
import fi.ambientia.atlassian.routes.feature_battles.FeatureBattleWins;
import fi.ambientia.atlassian.routes.feature_battles.FeatureBattles;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import fi.ambientia.abtesting.infrastructure.WrappingActiveObjects;
import ut.fi.ambientia.abtesting.model.TestData;
import ut.fi.ambientia.e2e.bootstrap.Bootstrap;
import ut.fi.ambientia.helpers.TestPluginProperties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static ut.fi.ambientia.mocks.Dummy.dummy;

@RunWith(ActiveObjectsJUnitRunner.class)
public class Acc_ShowFeatureBattleForUserShould {


    public static final int CONSTANT_BIG_ENOUGH_TO_HAVE_NEW_AND_SHINY = 200;
    private static final int SMALL_ENOUGH_FOR_GOOD_OLD = -1;
    public static final UserIdentifier USERIDENTIFIER = TestData.USERIDENTIFIER;
    private final Bootstrap bootstrap = new Bootstrap();
    private EntityManager entityManager;
    private ActiveObjects ao;
    private TestPluginProperties properties;
    private FeatureBattleRoute featureBattleRoute;
    private FeatureBattles featureBattles;
    private DisplayFeatureBattle displayFeatureBattle;
    private FeatureBattleWins winFeatureBattle;

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

        featureBattleRoute = bootstrap.getFeatureBattleRoute();
        featureBattles = bootstrap.getFeatureBattles();
        displayFeatureBattle = bootstrap.getDisplayBattle();

        winFeatureBattle = bootstrap.getFeatureBattleWins();

    }

    @Test
    public void user_should_be_able_to_choose_a_feature_battle() throws MacroExecutionException {
        CreateNewFeatureBattleCommand featureBattleCommand =
                new CreateNewFeatureBattleCommand( TestData.FEATURE_BATTLE_IDENTIFIER.getFeatureBattleId(), SMALL_ENOUGH_FOR_GOOD_OLD, "Good Old", "Shiny new");
        featureBattles.createNew(dummy( HttpServletRequest.class), featureBattleCommand);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("feature_battle", TestData.FEATURE_BATTLE_IDENTIFIER.getFeatureBattleId());

        Response response = winFeatureBattle.createNew(dummy(HttpServletRequest.class), TestData.FEATURE_BATTLE_IDENTIFIER.getFeatureBattleId(), new FeatureBattleWinCommand(Experiment.Type.NEW_AND_SHINY, USERIDENTIFIER.getIdentifier()));

        String execute = displayFeatureBattle.execute(parameters, "", null);

        assertThat( response.getStatus(), equalTo(200));
        assertThat( execute, equalTo( String.format( Experiment.INCLUDE_PAGE, "FOOBAR", "Shiny new") ) );
    }
}
