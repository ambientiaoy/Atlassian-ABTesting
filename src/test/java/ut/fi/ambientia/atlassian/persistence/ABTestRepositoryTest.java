package ut.fi.ambientia.atlassian.persistence;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.repository.AbTestInstanceRepository;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(ActiveObjectsJUnitRunner.class)
public class ABTestRepositoryTest {

    private EntityManager entityManager;

    private ActiveObjects ao; // (1)

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        AbTestInstanceRepository repository = new AbTestInstanceRepository(ao);
    }

    @Test
    public void should() throws Exception {

    }
}
