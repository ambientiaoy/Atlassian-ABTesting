package fi.ambientia.atlassian.routes.feature_battles;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.abtesting.action.UpdateFeatureBattle;
import fi.ambientia.atlassian.routes.arguments.UpdateFeatureBattleThresholdCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/feature_battle/{featureBattleId}/threshold")
public class FeatureBattleThresholdRoute {

    private final UpdateFeatureBattle updateFeatureBattle;

    @Autowired
    public FeatureBattleThresholdRoute(UpdateFeatureBattle updateFeatureBattle) {
        this.updateFeatureBattle = updateFeatureBattle;
    }

    @PublicApi
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response update(@Context HttpServletRequest httpServletRequest, UpdateFeatureBattleThresholdCommand command) {

        updateFeatureBattle.threshold(command);
        return Response.status(200).build();
    }

}
