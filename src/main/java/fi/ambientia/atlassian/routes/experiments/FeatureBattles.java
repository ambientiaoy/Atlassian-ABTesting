package fi.ambientia.atlassian.routes.experiments;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattle;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.atlassian.resouces.FeatureBattleResource;
import fi.ambientia.atlassian.routes.arguments.JsonFeatureBattleArgument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Arrays;

@Controller
@Path("/feature_battles")
public class FeatureBattles {

    // FeatureBattle

    public static final String ROUTE_ROOT = "/ABTest/";
    private CreateExperiment createNewHypothesis;
    private fi.ambientia.atlassian.routes.experiments.FeatureBattleRoute featureBattle;

    @Autowired
    public FeatureBattles(CreateExperiment createNewHypothesis, fi.ambientia.atlassian.routes.experiments.FeatureBattleRoute featureBattle) {
        this.createNewHypothesis = createNewHypothesis;
        this.featureBattle = featureBattle;
    }


    @PublicApi
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll(@Context HttpServletRequest httpServletRequest){
        return Response.ok( Arrays.asList(
                new FeatureBattleResource("FOOBAR", 10, "Good Old", "Fancy new"),
                new FeatureBattleResource("Baz", 90, "Same Old", "Something blue"),
                new FeatureBattleResource("FOOBAR", 60, "Oldie", "something borrowed")

        )).build();
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
