package fi.ambientia.atlassian.routes.experiments;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
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

@Controller
@Path("/experiments/feature_battle/{featureBattleId}")
public class FeatureBattle {

    public static final String ROUTE_ROOT = "/ABTest/";
    private CreateExperiment createNewHypothesis;

    @Autowired
    public FeatureBattle(CreateExperiment createNewHypothesis) {
        this.createNewHypothesis = createNewHypothesis;
    }

    @PublicApi
    @HEAD
    @Produces({MediaType.APPLICATION_JSON})
    public Response head(@Context HttpServletRequest request, @PathParam("featureBattleId") String featureBattleId){
        throw new UnsupportedOperationException();
    }
}
