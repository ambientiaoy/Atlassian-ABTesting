package it.fi.ambientia.abtesting.infrastructure.repositories.persistence;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.AbTestInstanceRepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ABTestAo;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(ActiveObjectsJUnitRunner.class)
public class ABTestRepositoryTest {

    private EntityManager entityManager;

    private ActiveObjects ao; // (1)
    private AbTestInstanceRepository repository;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        ao.migrate(ABTestAo.class);
        repository = new AbTestInstanceRepository(ao);
    }

    @Ignore
    @Test
    public void should() throws Exception {
        ABTestAo[] abTestAos = ao.find(ABTestAo.class);

        assertThat(abTestAos.length, equalTo(1));
    }
}
