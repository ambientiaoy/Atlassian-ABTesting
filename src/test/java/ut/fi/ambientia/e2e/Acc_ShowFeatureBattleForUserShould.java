package ut.fi.ambientia.e2e;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseExperiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.CreateNewFeatureBattle;
import fi.ambientia.abtesting.action.experiments.feature_battles.ExecuteFeatureBattle;
import fi.ambientia.abtesting.action.experiments.feature_battles.RandomizeFeatureBattle;
import fi.ambientia.abtesting.infrastructure.repositories.AbTestInstanceRepository;
import fi.ambientia.abtesting.infrastructure.repositories.ExperimentRepository;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.atlassian.routes.arguments.JsonFeatureBattleArgument;
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
        properties = new TestPluginProperties();


        FeatureBattleAORepository featureBattleRepository = new FeatureBattleAORepository(ao, properties);
        ExperimentRepository experimentRepository = new ExperimentRepository(ao, properties);

        RandomizeFeatureBattle randomizeFeatureBattle = new RandomizeFeatureBattle( featureBattleRepository );
        ExecuteFeatureBattle executeFeatureBattle = new ExecuteFeatureBattle(featureBattleRepository);
        AlreadyDecidedBattles alreadyDecidedBattles = new AlreadyDecidedBattles(featureBattleRepository);

        chooseExperiment = new ChooseExperiment( alreadyDecidedBattles, executeFeatureBattle, randomizeFeatureBattle);

        AbTestInstanceRepository abTestInstanceRepository = new AbTestInstanceRepository(ao);
        CreateExperiment createExperiment = new CreateNewFeatureBattle( abTestInstanceRepository );
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

        JsonFeatureBattleArgument newAbTest = new JsonFeatureBattleArgument( TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier(), SMALL_ENOUGH_FOR_GOOD_OLD);
        featureBattles.createNew(dummy( HttpServletRequest.class), newAbTest);

        Experiment experiment = chooseExperiment.forUser( TestData.USERIDENTIFIER, TestData.FEATURE_BATTLE_IDENTIFIER);

        assertThat( experiment.type(), equalTo(Experiment.Type.GOOD_OLD));
    }


}
