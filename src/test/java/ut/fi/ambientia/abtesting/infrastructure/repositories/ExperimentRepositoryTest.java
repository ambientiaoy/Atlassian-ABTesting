package ut.fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.ExperimentRepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ut.fi.ambientia.abtesting.model.TestData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(ActiveObjectsJUnitRunner.class)
public class ExperimentRepositoryTest {

    private PluginProperties properties;
    private ActiveObjects ao;
    private EntityManager entityManager;
    private ExperimentRepository experimentRepository;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        ao.migrate(ExperimentAO.class);
        properties = new PluginProperties(){
            @Override
            protected String getApplicationHome() {
                return this.getClass().getResource("/").getPath();
            }
        };
        experimentRepository = new ExperimentRepository(ao, properties);
    }

    @Test
    public void should_create_new_experiment_with_default_threshold() throws Exception {
        experimentRepository.createExperiment( createExperimentIdentifier() );

        ExperimentAO[] experimentAOs = ao.find(ExperimentAO.class);

        assertOnlyOneAOWithIdentifierAndThreshold(experimentAOs, TestData.EXPERIMENT_IDENTIFIER, ExperimentRepository.DEFAULT_THRESHOLD);
    }

    protected void assertOnlyOneAOWithIdentifierAndThreshold(ExperimentAO[] experimentAOs, ExperimentIdentifier identifier, int threshold) {
        assertThat( experimentAOs.length, equalTo(1));
        assertThat( experimentAOs[0].getExperimentId(), equalTo(identifier.getIdentifier() ) );
        assertThat( experimentAOs[0].getThreshold(), equalTo(threshold) );
    }

    @Test
    public void should_set_threshold() throws Exception {
        ExperimentIdentifier experimentIdentifier = createExperimentIdentifier();
        experimentRepository.createExperiment(experimentIdentifier);
        experimentRepository.setThreshold( experimentIdentifier, 15 );

        ExperimentAO[] experimentAOs = ao.find(ExperimentAO.class);

        assertOnlyOneAOWithIdentifierAndThreshold(experimentAOs, TestData.EXPERIMENT_IDENTIFIER, 15);
    }

    @Test
    public void should_not_store_same_identifier_more_than_once() throws Exception {
        ExperimentIdentifier experimentIdentifier = createExperimentIdentifier();
        experimentRepository.createExperiment(experimentIdentifier);
        experimentRepository.setThreshold( experimentIdentifier, 15 );
        experimentRepository.createExperiment(experimentIdentifier);
        experimentRepository.createExperiment(experimentIdentifier);

        ExperimentAO[] experimentAOs = ao.find(ExperimentAO.class);

        assertOnlyOneAOWithIdentifierAndThreshold(experimentAOs, TestData.EXPERIMENT_IDENTIFIER, 15);
    }

    private ExperimentIdentifier createExperimentIdentifier() {
        return TestData.EXPERIMENT_IDENTIFIER;
    }
}