package fi.ambientia.atlassian.routes.experiments;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.atlassian.routes.arguments.JsonFeatureBattleArgument;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/experiments/feature_battles")
public class FeatureBattles {

    // FeatureBattle

    public static final String ROUTE_ROOT = "/ABTest/";
    private CreateExperiment createNewHypothesis;
    private fi.ambientia.atlassian.routes.experiments.FeatureBattle featureBattle;

    public FeatureBattles(CreateExperiment createNewHypothesis, fi.ambientia.atlassian.routes.experiments.FeatureBattle featureBattle) {
        this.createNewHypothesis = createNewHypothesis;
        this.featureBattle = featureBattle;
    }

    @PublicApi
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response createNew(@Context HttpServletRequest httpServletRequest, JsonFeatureBattleArgument newAbTest) {
        Response head = featureBattle.head(httpServletRequest, newAbTest.getUniqueKey());

        if (head.getStatus() == Response.Status.OK.getStatusCode()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        createNewHypothesis.createNew(newAbTest);
        URI location = URI.create(ROUTE_ROOT + newAbTest.getUniqueKey());
        return Response.created(location).build();
    }
}
