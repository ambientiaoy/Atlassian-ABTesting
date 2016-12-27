package ut.fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.ExperimentAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
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
    private ExperimentAORepository experimentRepository;
    private FeatureBattleAORepository featureBattleRepository;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        ao.migrate(ExperimentAO.class);
        ao.migrate(FeatureBattleAO.class);
        properties = new PluginProperties(){
            @Override
            protected String getApplicationHome() {
                return this.getClass().getResource("/").getPath();
            }
        };
        experimentRepository = new ExperimentAORepository(ao, properties);
        featureBattleRepository = new FeatureBattleAORepository(ao, properties);
    }

    @Test
    public void should_create_new_experiment_with_default_threshold() throws Exception {
        featureBattleRepository.createFeatureBattle( createExperimentIdentifier() );

        FeatureBattleAO[] experimentAOs = ao.find(FeatureBattleAO.class);

        assertOnlyOneAOWithIdentifierAndThreshold(experimentAOs, TestData.FEATURE_BATTLE_IDENTIFIER, FeatureBattleAORepository.DEFAULT_THRESHOLD);
    }

    protected void assertOnlyOneAOWithIdentifierAndThreshold(FeatureBattleAO[] experimentAOs, FeatureBattleIdentifier identifier, int threshold) {
        assertThat( experimentAOs.length, equalTo(1));
        assertThat( experimentAOs[0].getFeatureBattleId(), equalTo(identifier.getIdentifier() ) );
        assertThat( experimentAOs[0].getThreshold(), equalTo(threshold) );
    }

    @Test
    public void should_set_threshold() throws Exception {
        FeatureBattleIdentifier featureBattleIdentifier = createExperimentIdentifier();
        featureBattleRepository.createFeatureBattle(featureBattleIdentifier);
        featureBattleRepository.setThreshold(featureBattleIdentifier, 15 );

        FeatureBattleAO[] experimentAOs = ao.find(FeatureBattleAO.class);

        assertOnlyOneAOWithIdentifierAndThreshold(experimentAOs, TestData.FEATURE_BATTLE_IDENTIFIER, 15);
    }

    @Test
    public void should_not_store_same_identifier_more_than_once() throws Exception {
        FeatureBattleIdentifier featureBattleIdentifier = createExperimentIdentifier();
        featureBattleRepository.createFeatureBattle(featureBattleIdentifier);
        featureBattleRepository.setThreshold(featureBattleIdentifier, 15 );
        featureBattleRepository.createFeatureBattle(featureBattleIdentifier);
        featureBattleRepository.createFeatureBattle(featureBattleIdentifier);

        FeatureBattleAO[] experimentAOs = ao.find(FeatureBattleAO.class);

        assertOnlyOneAOWithIdentifierAndThreshold(experimentAOs, TestData.FEATURE_BATTLE_IDENTIFIER, 15);
    }

    private FeatureBattleIdentifier createExperimentIdentifier() {
        return TestData.FEATURE_BATTLE_IDENTIFIER;
    }
}