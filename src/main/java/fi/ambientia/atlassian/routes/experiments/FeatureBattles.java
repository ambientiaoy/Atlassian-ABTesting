package fi.ambientia.atlassian.routes.experiments;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattle;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.atlassian.resouces.FeatureBattleResource;
import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Path("/feature_battles")
public class FeatureBattles {

    // FeatureBattle

    public static final String ROUTE_ROOT = "/ABTest/";
    private CreateExperiment createExperiment;
    private FeatureBattleRoute featureBattle;
    private final FeatureBattleRepository featureBattleRepository;

    @Autowired
    public FeatureBattles(CreateExperiment createExperiment, FeatureBattleRoute featureBattle, FeatureBattleRepository featureBattleRepository) {
        this.createExperiment = createExperiment;
        this.featureBattle = featureBattle;
        this.featureBattleRepository = featureBattleRepository;
    }


    @PublicApi
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll(@Context HttpServletRequest httpServletRequest){

        List<FeatureBattle> all = featureBattleRepository.getAll();

        List<FeatureBattleResource> collect = all.stream().
                map(featureBattle -> new FeatureBattleResource(
                        featureBattle.getId(),
                        featureBattle.getThreshold(),
                        featureBattle.getGooldOld(),
                        featureBattle.getNewAndShiny())).
                collect(Collectors.toList());

        return Response.ok( collect ).build();
    }

    @PublicApi
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response createNew(@Context HttpServletRequest httpServletRequest, CreateNewFeatureBattleCommand newAbTest) {
        return featureBattle.createNew(httpServletRequest, newAbTest);
    }


}
