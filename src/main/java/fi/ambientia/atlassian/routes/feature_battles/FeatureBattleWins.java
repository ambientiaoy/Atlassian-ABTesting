package fi.ambientia.atlassian.routes.feature_battles;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.abtesting.action.ChooseAWinnerOfAFeatureBattle;
import fi.ambientia.abtesting.events.ChooseAWinnerEvent;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.routes.arguments.FeatureBattleWinCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/feature_battle/{feature_battle_id}/users")
public class FeatureBattleWins {

    private final FeatureBattleRoute featureBattleRoute;
    private final ChooseAWinnerOfAFeatureBattle chooseAWinnerOfAFeatureBattle;

    @Autowired
    public FeatureBattleWins(FeatureBattleRoute featureBattleRoute, ChooseAWinnerOfAFeatureBattle chooseAWinnerOfAFeatureBattle) {
        this.featureBattleRoute = featureBattleRoute;
        this.chooseAWinnerOfAFeatureBattle = chooseAWinnerOfAFeatureBattle;
    }

    @PublicApi
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createNew(@Context HttpServletRequest httpServletRequest, @PathParam("feature_battle_id") String featureBattleId, FeatureBattleWinCommand featureBattleWinCommand) {
        Response head = featureBattleRoute.head( httpServletRequest, featureBattleId);

        if (head.getStatus() != Response.Status.OK.getStatusCode()) {
            return head;
        }

        UserIdentifier userIdentifier = featureBattleWinCommand.getUserIdentifier();
        FeatureBattleIdentifier featureBattleIdentifier = new FeatureBattleIdentifier( featureBattleId );
        chooseAWinnerOfAFeatureBattle.forFeatureBattle(new ChooseAWinnerEvent( userIdentifier, featureBattleIdentifier, featureBattleWinCommand.getType()) );

        return Response.ok().build();
    }
}
