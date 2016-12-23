package ut.fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ABTestAo;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRandomizer;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ut.fi.ambientia.abtesting.model.experiments.ChooseExperimentShould;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(ActiveObjectsJUnitRunner.class)
public class FeatureBattleAORepositoryTest {

    public static final int CONSTANT_SMALL_ENOUGH_SO_THAT_ALWAYS_TAKES_THE_OLD = 0;
    public static final int CONSTANT_BIG_ENOUGH_TO_ALWAYS_TRY_THE_NEW = 200;
    private EntityManager entityManager;

    private ActiveObjects ao; // (1)
    private FeatureBattleAORepository repository;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        ao.migrate(ABTestAo.class);
        PluginProperties properties = new PluginProperties(){
            @Override
            protected String getApplicationHome() {
                return this.getClass().getResource("/").getPath();
            }
        };
        repository = new FeatureBattleAORepository(ao, properties);
    }

    @Test
    public void should_get_default_random_value_if_random_percentage_not_defined() throws Exception {
        PluginProperties properties = mock(PluginProperties.class);
        repository = new FeatureBattleAORepository(ao, properties);
        when( properties.propertyOrDefault("feature.battle.default.win", 25)).
                thenReturn(
                        CONSTANT_SMALL_ENOUGH_SO_THAT_ALWAYS_TAKES_THE_OLD,
                        CONSTANT_BIG_ENOUGH_TO_ALWAYS_TRY_THE_NEW);
        ExperimentRandomizer experimentRandomizer = repository.experimentRandomizer(ChooseExperimentShould.EXPERIMENT_IDENTIFIER);

        Experiment experiment = experimentRandomizer.randomize();
        assertThat(experiment.type(), equalTo(Experiment.Type.GOOD_OLD));

        assertThat(experimentRandomizer.randomize().type(), equalTo(Experiment.Type.NEW_AND_SHINY));
    }

    @Test
    public void should_store_randomized_experiment_for_user() throws Exception {


    }
}