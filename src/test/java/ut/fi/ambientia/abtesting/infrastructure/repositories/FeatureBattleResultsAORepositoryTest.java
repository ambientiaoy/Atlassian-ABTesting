package ut.fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.infrastructure.WrappingActiveObjects;
import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleResultsAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleResultAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ut.fi.ambientia.abtesting.model.TestData;
import ut.fi.ambientia.helpers.TestPluginProperties;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static ut.fi.ambientia.abtesting.model.TestData.FEATURE_BATTLE_IDENTIFIER;

@RunWith(ActiveObjectsJUnitRunner.class)
public class FeatureBattleResultsAORepositoryTest {

    public static final int CONSTANT_SMALL_ENOUGH_SO_THAT_ALWAYS_TAKES_THE_OLD = 0;
    public static final int CONSTANT_BIG_ENOUGH_TO_ALWAYS_TRY_THE_NEW = 200;
    private EntityManager entityManager;

    private ActiveObjects ao; // (1)

    TestPluginProperties properties;
    private FeatureBattleResults featureBattles;

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
        properties = new TestPluginProperties();
        featureBattles = new FeatureBattleResultsAORepository(sao, properties);
    }

    @Test
    public void should_create_a_list_of_two_with_null_users() throws Exception {
        FeatureBattleAO featureBattleAO = ao.create(FeatureBattleAO.class);
        featureBattleAO.setFeatureBattleId( TestData.FEATURE_BATTLE_IDENTIFIER.getFeatureBattleId() );
        featureBattleAO.save();
        createExperiment(featureBattleAO, Experiment.Type.GOOD_OLD);
        createExperiment(featureBattleAO, Experiment.Type.NEW_AND_SHINY);

        List<FeatureBattleResult> featureBattleResults = featureBattles.featureBattleResultsFor(TestData.FEATURE_BATTLE_IDENTIFIER);

        assertThat( featureBattleResults.size(), equalTo(2));

    }


    @Test
    public void should_store_winning_feature_battle_for_user() throws Exception {


    }

    protected void createExperiment(FeatureBattleAO featureBattleAO, Experiment.Type type) {
        ExperimentAO experimentAO = ao.create(ExperimentAO.class);
        experimentAO.setFeatureBattle( featureBattleAO );
        experimentAO.setExperimentId( FEATURE_BATTLE_IDENTIFIER.getFeatureBattleId());
        experimentAO.setPage("PAGE");
        experimentAO.setExperimentType(type);
        experimentAO.save();
    }
}