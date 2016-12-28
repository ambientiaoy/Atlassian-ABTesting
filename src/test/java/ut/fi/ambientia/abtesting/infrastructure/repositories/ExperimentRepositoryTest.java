package ut.fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.ExperimentAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ut.fi.ambientia.abtesting.model.TestData;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static ut.fi.ambientia.abtesting.model.TestData.FEATURE_BATTLE_IDENTIFIER;
import static ut.fi.ambientia.abtesting.model.TestData.USERIDENTIFIER;
import static ut.fi.ambientia.matchers.ExperimentMatcher.thatIsEqualTo;

@RunWith(ActiveObjectsJUnitRunner.class)
public class ExperimentRepositoryTest {

    private PluginProperties properties;
    private ActiveObjects ao;
    private EntityManager entityManager;
    private ExperimentAORepository experimentRepository;
    private FeatureBattleAORepository featureBattleRepository;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        ao.migrate(ExperimentAO.class);
        ao.migrate(FeatureBattleAO.class);
        ao.migrate(UserExperimentAO.class);
        properties = new PluginProperties(){
            @Override
            protected String getApplicationHome() {
                return this.getClass().getResource("/").getPath();
            }
        };
        experimentRepository = new ExperimentAORepository(ao, properties);
        featureBattleRepository = new FeatureBattleAORepository(ao, properties, experimentRepository);
    }

    @Test
    public void should_create_new_experiments() throws Exception {
        FeatureBattleAO featureBattleAO = ao.create(FeatureBattleAO.class);
        featureBattleAO.setFeatureBattleId( FEATURE_BATTLE_IDENTIFIER.getIdentifier());
        featureBattleAO.save();

        Integer page = experimentRepository.create(featureBattleAO.getID(), Experiment.Type.GOOD_OLD).forPage("page").andGetId();

        ExperimentAO[] experimentAOs = ao.find(ExperimentAO.class);

        assertThat( experimentAOs.length, equalTo(1));
        assertThat( experimentAOs[0].getFeatureBattle().getFeatureBattleId(), equalTo(FEATURE_BATTLE_IDENTIFIER.getIdentifier() ) );
        assertThat( experimentAOs[0].getExperimentType(), equalTo(Experiment.Type.GOOD_OLD));
        assertThat( experimentAOs[0].getPage(), equalTo("page"));

    }

    @Test
    public void should_get_experiment_for_user() throws Exception {
        Experiment experiment = new NewAndShiny(FEATURE_BATTLE_IDENTIFIER);

        FeatureBattleAO featureBattleAO = ao.create(FeatureBattleAO.class);
        featureBattleAO.setFeatureBattleId( FEATURE_BATTLE_IDENTIFIER.getIdentifier());
        featureBattleAO.save();

        Integer page = experimentRepository.create(featureBattleAO.getID(), Experiment.Type.NEW_AND_SHINY).forPage("page").andGetId();

        featureBattleRepository.newFeatureBattleFor(FEATURE_BATTLE_IDENTIFIER).forUser(  USERIDENTIFIER ).resultBeing(experiment);

        List<Experiment> experiments = experimentRepository.experimentsForUser(USERIDENTIFIER);

        MatcherAssert.assertThat(experiments, hasItem( thatIsEqualTo(experiment, FEATURE_BATTLE_IDENTIFIER)));
    }


    private Experiment goodOld(FeatureBattleIdentifier featureBattleIdentifier) {
        return new GoodOldWay(featureBattleIdentifier);
    }

    private FeatureBattleIdentifier createExperimentIdentifier() {
        return TestData.FEATURE_BATTLE_IDENTIFIER;
    }
}