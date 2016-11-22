package fi.ambientia.atlassian.resource;

import com.atlassian.annotations.PublicApi;
import fi.ambientia.atlassian.action.CreateHypothesis;
import fi.ambientia.atlassian.model.ABTestInstance;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

public class ABTest {

    private CreateHypothesis createNewHypothesis;

    public ABTest(CreateHypothesis createNewHypothesis) {

        this.createNewHypothesis = createNewHypothesis;
    }

    @PublicApi
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public void createNew(ABTestInstance newAbTest) {
        createNewHypothesis.createNew(newAbTest);
    }
}
