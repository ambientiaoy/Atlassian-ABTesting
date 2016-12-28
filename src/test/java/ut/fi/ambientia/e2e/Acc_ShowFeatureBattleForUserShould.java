package ut.fi.ambientia.e2e;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseExperiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.CreateNewFeatureBattle;
import fi.ambientia.abtesting.action.experiments.feature_battles.ExecuteFeatureBattle;
import fi.ambientia.abtesting.action.experiments.feature_battles.RandomizeFeatureBattle;
import fi.ambientia.abtesting.infrastructure.repositories.ExperimentAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;
import fi.ambientia.atlassian.routes.experiments.FeatureBattleRoute;
import fi.ambientia.atlassian.routes.experiments.FeatureBattles;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ut.fi.ambientia.abtesting.model.TestData;
import ut.fi.ambientia.helpers.TestPluginProperties;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static ut.fi.ambientia.mocks.Dummy.dummy;

@RunWith(ActiveObjectsJUnitRunner.class)
public class Acc_ShowFeatureBattleForUserShould {


    public static final int CONSTANT_BIG_ENOUGH_TO_HAVE_NEW_AND_SHINY = 200;
    private static final int SMALL_ENOUGH_FOR_GOOD_OLD = -1;
    private EntityManager entityManager;
    private ActiveObjects ao;
    private TestPluginProperties properties;
    private ChooseExperiment chooseExperiment;
    private FeatureBattleRoute featureBattleRoute;
    private FeatureBattles featureBattles;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        ao.migrate(UserExperimentAO.class);
        ao.migrate(ExperimentAO.class);
        ao.migrate(FeatureBattleAO.class);
        properties = new TestPluginProperties();


        FeatureBattleAORepository featureBattleRepository = new FeatureBattleAORepository(ao, properties);
        ExperimentAORepository experimentRepository = new ExperimentAORepository(ao, properties);

        RandomizeFeatureBattle randomizeFeatureBattle = new RandomizeFeatureBattle( featureBattleRepository, experimentRepository );
        ExecuteFeatureBattle executeFeatureBattle = new ExecuteFeatureBattle(featureBattleRepository, experimentRepository);
        AlreadyDecidedBattles alreadyDecidedBattles = new AlreadyDecidedBattles(featureBattleRepository);

        chooseExperiment = new ChooseExperiment( alreadyDecidedBattles, executeFeatureBattle, randomizeFeatureBattle);

        CreateExperiment createExperiment = new CreateNewFeatureBattle( featureBattleRepository );
        featureBattleRoute = new FeatureBattleRoute(createExperiment, featureBattleRepository);

        featureBattles = new FeatureBattles(createExperiment, featureBattleRoute);
    }

    @Test
    public void select_one_randomly_if_no_feature_battles_have_been_stored() throws Exception {
        Experiment experiment;

        properties.setProperty("feature.battle.default.win", CONSTANT_BIG_ENOUGH_TO_HAVE_NEW_AND_SHINY);
        experiment = chooseExperiment.forUser( TestData.USERIDENTIFIER, TestData.FEATURE_BATTLE_IDENTIFIER);
        assertThat( experiment.type(), equalTo(Experiment.Type.NEW_AND_SHINY));

        properties.setProperty("feature.battle.default.win", SMALL_ENOUGH_FOR_GOOD_OLD);
        experiment = chooseExperiment.forUser( TestData.USERIDENTIFIER, TestData.FEATURE_BATTLE_IDENTIFIER);
        assertThat( experiment.type(), equalTo(Experiment.Type.GOOD_OLD));
    }

    @Test
    public void by_default_user_will_get_a_feature_battle_result_that_is_defined_when_feature_battle_is_created(){
        properties.setProperty("feature.battle.default.win", CONSTANT_BIG_ENOUGH_TO_HAVE_NEW_AND_SHINY);

        CreateNewFeatureBattleCommand newAbTest = new CreateNewFeatureBattleCommand( TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier(), SMALL_ENOUGH_FOR_GOOD_OLD, "Good Old", "Shiny new");
        featureBattles.createNew(dummy( HttpServletRequest.class), newAbTest);

        Experiment experiment = chooseExperiment.forUser( TestData.USERIDENTIFIER, TestData.FEATURE_BATTLE_IDENTIFIER);

        assertThat( experiment.type(), equalTo(Experiment.Type.GOOD_OLD));
        assertThat( experiment.render(), equalTo(String.format(Experiment.INCLUDE_PAGE, Experiment.ABTEST, "Good Old")));
    }

    @Test
    public void can_create_experiment_and_is_shown_in_rest_api() throws Exception {
        fail("ToBeDefined");
    }

    @Test
    public void can_update_experiment() throws Exception {
        fail("ToBeDefined");
    }

    @Test
    public void can_mark_experiment_as_done(){
        // not Delete, mark as done, choose either experiment.
        fail("ToBeDefined");
    }
}
