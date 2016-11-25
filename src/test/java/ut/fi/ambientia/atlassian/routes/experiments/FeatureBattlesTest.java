package ut.fi.ambientia.atlassian.routes.experiments;

import fi.ambientia.atlassian.action.CreateHypothesis;
import fi.ambientia.abtesting.model.ABTestInstance;
import fi.ambientia.atlassian.routes.experiments.FeatureBattles;
import fi.ambientia.atlassian.routes.GetABTestRoute;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FeatureBattlesTest {

    public final String AB_INSTANCE_UNIQUE_KEY = "KEY";
    private CreateHypothesis createNewHypothesis;
    private FeatureBattles featureBattles;
    private ABTestInstance newAbTest;
    private GetABTestRoute getAbTestRoute;
    private HttpServletRequest context = mock(HttpServletRequest.class);

    @Before
    public void setUp() throws Exception {
        createNewHypothesis = mock(CreateHypothesis.class);
        getAbTestRoute = mock(GetABTestRoute.class);
        featureBattles = new FeatureBattles(createNewHypothesis, getAbTestRoute);
        newAbTest = new ABTestInstance(AB_INSTANCE_UNIQUE_KEY);

        Response response_not_found = Response.status(Response.Status.NOT_FOUND ).build();
        when(getAbTestRoute.head(any(HttpServletRequest.class), argThat(equalTo(AB_INSTANCE_UNIQUE_KEY)))).thenReturn( response_not_found );
    }

    @Test
    public void shouldStoreNewAb() throws Exception {
        // act
        Response response = featureBattles.createNew(context, newAbTest);
        // assert
        verify(createNewHypothesis).createNew(newAbTest);
        assertThat(response.getStatus(), equalTo(201));
    }

    @Test
    public void shouldHaveLocation() throws Exception {
        // act
        Response response = featureBattles.createNew(context, newAbTest);

        // assert
        verify( createNewHypothesis ).createNew( newAbTest );
        assertThat(response.getMetadata().getFirst("location").toString(), equalTo(FeatureBattles.ROUTE_ROOT  + AB_INSTANCE_UNIQUE_KEY));
    }

    @Test
    public void shouldFailWhenResourceAlreadyExists() throws Exception {
        // arrange
        Response response_ok = Response.status(Response.Status.OK ).build();
        when(getAbTestRoute.head(any(HttpServletRequest.class), argThat(equalTo(AB_INSTANCE_UNIQUE_KEY)))).thenReturn( response_ok );

        Response response = featureBattles.createNew(context,  newAbTest );

        assertThat( response.getStatus(), equalTo(400));
    }
}