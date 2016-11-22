package ut.fi.ambientia.atlassian.resource;

import fi.ambientia.atlassian.action.CreateHypothesis;
import fi.ambientia.atlassian.model.ABTestInstance;
import fi.ambientia.atlassian.resource.ABTestRoute;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ABTestTest {

    public final String AB_INSTANCE_UNIQUE_KEY = "KEY";
    private CreateHypothesis createNewHypothesis;
    private ABTestRoute abTestRoute;
    private ABTestInstance newAbTest;

    @Before
    public void setUp() throws Exception {

        createNewHypothesis = mock(CreateHypothesis.class);
        abTestRoute = new ABTestRoute(createNewHypothesis);
        newAbTest = new ABTestInstance(AB_INSTANCE_UNIQUE_KEY);

    }

    @Test
    public void shouldStoreNewAb() throws Exception {
        // act
        Response response = abTestRoute.createNew(newAbTest);
        // assert
        verify(createNewHypothesis).createNew(newAbTest);
        assertThat(response.getStatus(), equalTo(201));
    }

    @Test
    public void shouldHaveLocation() throws Exception {
        // act
        Response response = abTestRoute.createNew(newAbTest);

        // assert
        verify( createNewHypothesis ).createNew( newAbTest );
        assertThat(response.getMetadata().getFirst("location").toString(), equalTo(ABTestRoute.ROUTE_ROOT  + AB_INSTANCE_UNIQUE_KEY));
    }
}