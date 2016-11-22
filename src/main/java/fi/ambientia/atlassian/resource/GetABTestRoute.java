package fi.ambientia.atlassian.resource;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.atlassian.action.CreateHypothesis;
import fi.ambientia.atlassian.model.ABTestInstance;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

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
