package fi.ambientia.atlassian.routes.feature_battles;

import fi.ambientia.atlassian.routes.arguments.FeatureBattleWinCommand;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Controller
@Path("/feature_battle/{feature_battle_id}/users")
public class FeatureBattleWins {


    public Response createNew(HttpServletRequest httpServletRequest, @PathParam("feature_battle_id") String feature_battle_id, FeatureBattleWinCommand featureBattleWinCommand) {
        return null;
    }
}
