package fi.ambientia.atlassian.routes.experiments;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.atlassian.action.CreateHypothesis;
import fi.ambientia.abtesting.model.ABTestInstance;
import fi.ambientia.atlassian.routes.GetABTestRoute;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

public class FeatureBattles {

    // FeatureBattle

    public static final String ROUTE_ROOT = "/ABTest/";
    private CreateHypothesis createNewHypothesis;
    private GetABTestRoute getAbTestRoute;

    public FeatureBattles(CreateHypothesis createNewHypothesis, GetABTestRoute getAbTestRoute) {

        this.createNewHypothesis = createNewHypothesis;
        this.getAbTestRoute = getAbTestRoute;
    }

    @PublicApi
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response createNew(@Context HttpServletRequest httpServletRequest, ABTestInstance newAbTest) {
        Response head = getAbTestRoute.head(httpServletRequest, newAbTest.getUniqueKey());

        if (head.getStatus() == Response.Status.OK.getStatusCode()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        createNewHypothesis.createNew(newAbTest);
        URI location = URI.create(ROUTE_ROOT + newAbTest.getUniqueKey());
        return Response.created(location).build();
    }
}
