package fi.ambientia.atlassian.routes.feature_battles;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattle;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Optional;

@Controller
@Path("/feature_battle")
public class FeatureBattleRoute {

    public static final String ROUTE_ROOT = "/ABTest/";
    private CreateExperiment createExperiment;
    private final FeatureBattleRepository featureBattleRepository;

    @Autowired
    public FeatureBattleRoute(CreateExperiment createExperiment, FeatureBattleRepository featureBattleRepository) {
        this.createExperiment = createExperiment;
        this.featureBattleRepository = featureBattleRepository;
    }

    @Path("{featureBattleId}")
    @PublicApi
    @HEAD
    @Produces({MediaType.APPLICATION_JSON})
    public Response head(@Context HttpServletRequest request, @PathParam("featureBattleId") String featureBattleId){
        Optional<FeatureBattle> featureBattle = featureBattleRepository.getFeatureBattle(new FeatureBattleIdentifier(featureBattleId));

        return featureBattle.map( featureBattle1 -> Response.ok().build() ).orElse( Response.status(Response.Status.NOT_FOUND ).build());
    }

    @PublicApi
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response createNew(@Context HttpServletRequest httpServletRequest, CreateNewFeatureBattleCommand newAbTest) {
        Response head = head(httpServletRequest, newAbTest.getUniqueKey());

        if (head.getStatus() == Response.Status.OK.getStatusCode()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        createExperiment.createNew(newAbTest);
        URI location = URI.create(ROUTE_ROOT + newAbTest.getUniqueKey());
        return Response.created(location).build();
    }

    @PublicApi
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response sortOfPost(@Context HttpServletRequest httpServletRequest, CreateNewFeatureBattleCommand newAbTest) {
        return createNew(httpServletRequest, newAbTest);
    }

}
