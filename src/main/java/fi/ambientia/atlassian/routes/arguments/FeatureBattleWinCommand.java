package fi.ambientia.atlassian.routes.arguments;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class FeatureBattleWinCommand {

    private final String experimentType;
    private final String userIdentifier;

    @JsonCreator
    public FeatureBattleWinCommand(
            @JsonProperty("type") String experimentType,
            @JsonProperty("user") String userKey) {
        this.experimentType =  experimentType;
        this.userIdentifier = userKey;
    }

    public UserIdentifier getUserIdentifier() {
        return new UserIdentifier(userIdentifier);
    }

    public Experiment.Type getType() {
        return Experiment.Type.valueOf( experimentType.toUpperCase() );
    }
}
