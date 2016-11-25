package ut.fi.ambientia.abtesting.action;

import fi.ambientia.abtesting.infrastructure.repositories.AbTestInstanceRepository;
import fi.ambientia.atlassian.action.AbTestHypothesis;
import fi.ambientia.atlassian.action.CreateHypothesis;
import fi.ambientia.abtesting.model.ABTestInstance;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AbTestHypothesisTest {

    private CreateHypothesis hypothesis;
    private ABTestInstance abTestInstance;
    private AbTestInstanceRepository abTestInstanceRepository;

    @Before
    public void setUp() throws Exception {
        abTestInstanceRepository = mock(AbTestInstanceRepository.class);
        hypothesis = new AbTestHypothesis(abTestInstanceRepository);
        abTestInstance = new ABTestInstance("UNIQUE_KEY");

    }



    @Test
    public void shouldCreateNewInstance() throws Exception {

        hypothesis.createNew( abTestInstance );


        verify( abTestInstanceRepository ).createNew( abTestInstance );
    }
}