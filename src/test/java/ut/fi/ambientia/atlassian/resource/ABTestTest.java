package ut.fi.ambientia.atlassian.resource;

import fi.ambientia.atlassian.action.CreateHypothesis;
import fi.ambientia.atlassian.model.ABTestInstance;
import fi.ambientia.atlassian.resource.ABTest;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ABTestTest {


    @Test
    public void shouldStoreNewAbTest() throws Exception {
        // arrange
        CreateHypothesis createNewHypothesis = mock(CreateHypothesis.class);
        ABTest abTest = new ABTest( createNewHypothesis );
        ABTestInstance newAbTest = new ABTestInstance();

        // act
        abTest.createNew(newAbTest);

        // assert
        verify( createNewHypothesis ).createNew( newAbTest );
    }
}