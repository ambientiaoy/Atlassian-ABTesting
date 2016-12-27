package ut.fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.ExperimentRepository;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.ExperimentRandomizer;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static ut.fi.ambientia.abtesting.model.TestData.EXPERIMENT_IDENTIFIER;
import static ut.fi.ambientia.abtesting.model.TestData.USERIDENTIFIER;
import static ut.fi.ambientia.matchers.ExperimentMatcher.thatIsEqualTo;

@RunWith(ActiveObjectsJUnitRunner.class)
public class FeatureBattleAORepositoryTest {

    public static final int CONSTANT_SMALL_ENOUGH_SO_THAT_ALWAYS_TAKES_THE_OLD = 0;
    public static final int CONSTANT_BIG_ENOUGH_TO_ALWAYS_TRY_THE_NEW = 200;
    private EntityManager entityManager;

    private ActiveObjects ao; // (1)
    private FeatureBattleAORepository repository;
    private ExperimentRepository experimentRepository;
    private Experiment experiment;

    TestPluginProperties properties;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        ao.migrate(UserExperimentAO.class);
        ao.migrate(ExperimentAO.class);
        properties = new TestPluginProperties();
        repository = new FeatureBattleAORepository(ao, properties);
        experimentRepository = new ExperimentRepository(ao, properties);
    }

    @Test
    public void should_get_default_random_value_if_random_percentage_not_defined() throws Exception {
        ExperimentRandomizer experimentRandomizer = repository.experimentRandomizer(EXPERIMENT_IDENTIFIER);

        properties.setProperty("feature.battle.default.win", CONSTANT_SMALL_ENOUGH_SO_THAT_ALWAYS_TAKES_THE_OLD);
        assertThat(experimentRandomizer.randomize().type(), equalTo(Experiment.Type.GOOD_OLD));

        properties.setProperty("feature.battle.default.win", CONSTANT_BIG_ENOUGH_TO_ALWAYS_TRY_THE_NEW);
        assertThat(experimentRandomizer.randomize().type(), equalTo(Experiment.Type.NEW_AND_SHINY));
    }

    @Test
    public void should_get_experiment_treshhold_from_db() throws Exception {

        properties.setProperty("feature.battle.default.win", CONSTANT_SMALL_ENOUGH_SO_THAT_ALWAYS_TAKES_THE_OLD);
        experimentRepository.createExperiment( EXPERIMENT_IDENTIFIER );
        experimentRepository.setThreshold( EXPERIMENT_IDENTIFIER, CONSTANT_BIG_ENOUGH_TO_ALWAYS_TRY_THE_NEW);

        ExperimentRandomizer experimentRandomizer = repository.experimentRandomizer(EXPERIMENT_IDENTIFIER);
        Experiment experiment = experimentRandomizer.randomize();

        assertThat(experiment.type(), equalTo(Experiment.Type.NEW_AND_SHINY));
    }

    @Test
    public void should_store_randomized_experiment_for_user() throws Exception {
        UserExperimentAO[] abTestAos = ao.find(UserExperimentAO.class);
        assertThat(abTestAos.length, equalTo(0));

        repository.newFeatureBattleFor( EXPERIMENT_IDENTIFIER ).forUser(  USERIDENTIFIER ).resultBeing( newAndShiny( EXPERIMENT_IDENTIFIER) );

        abTestAos = ao.find(UserExperimentAO.class);
        assertThat(abTestAos.length, equalTo(1));
    }

    @Ignore
    @Test
    public void should_not_save_more_than_one_experiments() throws Exception {
        fail("NOT TESTED");
    }

    @Test
    public void should_get_experiment_for_user() throws Exception {
        Experiment experiment = newAndShiny(EXPERIMENT_IDENTIFIER);
        repository.newFeatureBattleFor( EXPERIMENT_IDENTIFIER ).forUser(  USERIDENTIFIER ).resultBeing(experiment);

        List<Experiment> experiments = repository.experimentsForUser(USERIDENTIFIER);

        assertThat(experiments, hasItem( thatIsEqualTo(experiment, EXPERIMENT_IDENTIFIER)));
    }

    private Experiment newAndShiny(ExperimentIdentifier experimentIdentifier) {
        return new NewAndShiny(experimentIdentifier);
    }

    private class TestPluginProperties extends PluginProperties {
        private final Map<String, Object> properties;
        private int threshold = ExperimentRepository.DEFAULT_THRESHOLD;

        private TestPluginProperties() {
            properties = new HashMap<>();
        }

        @Override
        protected String getApplicationHome() {
            return this.getClass().getResource("/").getPath();
        }


        public void setProperty(String property, int threshold){
            this.properties.put(property, threshold);
        }

        @Override
        public Integer propertyOrDefault(String key, int i) {
            Optional<Integer> optional = Optional.ofNullable((Integer) properties.get(key));
            return optional.orElse( i );
        }
    }
}