package fi.ambientia.atlassian.routes.experiments;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.model.experiments.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.experiments.FeatureBattleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Controller
@Path("/experiments/feature_battle/{featureBattleId}")
public class FeatureBattleRoute {

    public static final String ROUTE_ROOT = "/ABTest/";
    private CreateExperiment createNewHypothesis;
    private final FeatureBattleRepository featureBattleRepository;

    @Autowired
    public FeatureBattleRoute(CreateExperiment createNewHypothesis, FeatureBattleRepository featureBattleRepository) {
        this.createNewHypothesis = createNewHypothesis;
        this.featureBattleRepository = featureBattleRepository;
    }

    @PublicApi
    @HEAD
    @Produces({MediaType.APPLICATION_JSON})
    public Response head(@Context HttpServletRequest request, @PathParam("featureBattleId") String featureBattleId){

        Optional<fi.ambientia.abtesting.model.FeatureBattle> featureBattle = featureBattleRepository.getFeatureBattle(new FeatureBattleIdentifier(featureBattleId));

        return featureBattle.map( featureBattle1 -> Response.ok().build() ).orElse( Response.status(Response.Status.NOT_FOUND ).build());
    }
}
