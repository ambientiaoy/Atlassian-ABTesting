package ut.fi.ambientia.atlassian.routes.experiments;

import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;
import fi.ambientia.atlassian.routes.experiments.FeatureBattleRoute;
import fi.ambientia.atlassian.routes.experiments.FeatureBattles;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FeatureBattlesRouteShould {

    public final String AB_INSTANCE_UNIQUE_KEY = "KEY";
    private CreateExperiment createNewHypothesis;
    private FeatureBattles featureBattles;
    private CreateNewFeatureBattleCommand newAbTest;
    private fi.ambientia.atlassian.routes.experiments.FeatureBattleRoute featureBattle;
    private HttpServletRequest context = mock(HttpServletRequest.class);

    @Before
    public void setUp() throws Exception {
        createNewHypothesis = mock(CreateExperiment.class);
        FeatureBattleRepository featureBattleRepository = mock(FeatureBattleRepository.class);
        featureBattle = new FeatureBattleRoute(createNewHypothesis, featureBattleRepository);
        featureBattles = new FeatureBattles(createNewHypothesis, featureBattle, featureBattleRepository);
        newAbTest = new CreateNewFeatureBattleCommand(AB_INSTANCE_UNIQUE_KEY, 10, "Good Old", "Shiny new");

        Response response_not_found = Response.status(Response.Status.NOT_FOUND ).build();
        when(featureBattle.head(any(HttpServletRequest.class), argThat(equalTo(AB_INSTANCE_UNIQUE_KEY)))).thenReturn( response_not_found );
    }

    @Test
    public void shouldStoreNewAb() throws Exception {
        // act
        Response response = featureBattles.createNew(context, newAbTest);
        // assert
        verify(createNewHypothesis).createNew(newAbTest);
        assertThat(response.getStatus(), equalTo(201));
    }

    @Test
    public void shouldHaveLocation() throws Exception {
        // act
        Response response = featureBattles.createNew(context, newAbTest);

        // assert
        verify( createNewHypothesis ).createNew( newAbTest );
        assertThat(response.getMetadata().getFirst("location").toString(), equalTo(FeatureBattles.ROUTE_ROOT  + AB_INSTANCE_UNIQUE_KEY));
    }

    @Test
    public void shouldFailWhenResourceAlreadyExists() throws Exception {
        // arrange
        Response response_ok = Response.status(Response.Status.OK ).build();
        when(featureBattle.head(any(HttpServletRequest.class), argThat(equalTo(AB_INSTANCE_UNIQUE_KEY)))).thenReturn( response_ok );

        Response response = featureBattles.createNew(context,  newAbTest );

        assertThat( response.getStatus(), equalTo(400));
    }
}