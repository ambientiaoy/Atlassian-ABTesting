package fi.ambientia.atlassian.routes;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.atlassian.action.CreateHypothesis;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/abtest/{abTestId}/")
public class GetABTestRoute {

    public static final String ROUTE_ROOT = "/ABTest/";
    private CreateHypothesis createNewHypothesis;

    public GetABTestRoute(CreateHypothesis createNewHypothesis) {

        this.createNewHypothesis = createNewHypothesis;
    }

    @PublicApi
    @HEAD
    @Produces({MediaType.APPLICATION_JSON})
    public Response head(@Context HttpServletRequest request, @PathParam("abTestId") String abTestUniqueKey){
        throw new UnsupportedOperationException();
    }
}
