package ut.fi.ambientia.e2e;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseExperiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.CreateNewFeatureBattle;
import fi.ambientia.abtesting.action.experiments.feature_battles.ExecuteFeatureBattle;
import fi.ambientia.abtesting.action.experiments.feature_battles.RandomizeFeatureBattle;
import fi.ambientia.abtesting.infrastructure.WrappingActiveObjects;
import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.ExperimentAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleResultsAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleResultAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
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
import ut.fi.ambientia.abtesting.model.TestData;
import ut.fi.ambientia.helpers.TestPluginProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ut.fi.ambientia.mocks.Dummy.dummy;

@RunWith(ActiveObjectsJUnitRunner.class)
public class Acc_ChooseExperiment {


    public static final int CONSTANT_BIG_ENOUGH_TO_HAVE_NEW_AND_SHINY = 200;
    private static final int SMALL_ENOUGH_FOR_GOOD_OLD = -1;
    private HttpServletRequest httpServletRequestMock;
    private EntityManager entityManager;
    private ActiveObjects ao;
    private TestPluginProperties properties;
    private ChooseExperiment chooseExperiment;
    private FeatureBattleRoute featureBattleRoute;
    private FeatureBattles featureBattles;
    private DisplayFeatureBattle displayFeatureBattle;
    private Supplier<HttpServletRequest> supplier = () -> httpServletRequestMock;

    @Before
    public void setUp() throws Exception
    {
        httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getParameter("featureBattleWinner")).thenReturn(null);
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        ao.migrate(ExperimentAO.class);
        ao.migrate(FeatureBattleAO.class);
        ao.migrate(FeatureBattleResultAO.class);
        ao.migrate(UserExperimentAO.class);
        SimpleActiveObjects sao = new WrappingActiveObjects(ao);

        properties = new TestPluginProperties();


        ExperimentAORepository experimentRepository = new ExperimentAORepository(sao, properties);
        FeatureBattleAORepository featureBattleRepository = new FeatureBattleAORepository(sao, properties, experimentRepository);
        FeatureBattleResults featureBattleResults= new FeatureBattleResultsAORepository(sao, properties);

        RandomizeFeatureBattle randomizeFeatureBattle = new RandomizeFeatureBattle( featureBattleRepository, experimentRepository );
        ExecuteFeatureBattle executeFeatureBattle = new ExecuteFeatureBattle(featureBattleRepository, experimentRepository);
        AlreadyDecidedBattles alreadyDecidedBattles = new AlreadyDecidedBattles(featureBattleResults);

        chooseExperiment = new ChooseExperiment( alreadyDecidedBattles, executeFeatureBattle, randomizeFeatureBattle);

        CreateExperiment createExperiment = new CreateNewFeatureBattle( featureBattleRepository, experimentRepository);
        featureBattleRoute = new FeatureBattleRoute(createExperiment, featureBattleRepository);

        featureBattles = new FeatureBattles(createExperiment, featureBattleRoute, featureBattleRepository);

        UserManager userManager = mock(UserManager.class);
        when(userManager.getRemoteUserKey()).thenReturn( new UserKey("ANY USER"));

        displayFeatureBattle = new DisplayFeatureBattle(userManager, chooseExperiment, properties){
            @Override
            protected Supplier<Map<String, Object>> getVelocityContextSupplier() {
                return () -> {
                    HashMap<String, Object> objectObjectHashMap = new HashMap<>();
                    objectObjectHashMap.put("req", supplier.get());
                    return objectObjectHashMap;
                };
            }

            @Override
            protected Supplier<String> getRenderedTemplate(Map<String, Object> contextMap) {
                return () -> {
                    return contextMap.get("experiment") == null ?
                            "null" :
                            ((Experiment) contextMap.get("experiment")).render();
                };
            }
        };
        properties.setProperty("default.abtest.space.key", "FOOBAR");

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
