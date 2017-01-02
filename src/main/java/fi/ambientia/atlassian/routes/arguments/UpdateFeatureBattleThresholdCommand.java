package fi.ambientia.atlassian.routes.arguments;

import org.codehaus.jackson.annotate.JsonProperty;

public class UpdateFeatureBattleThresholdCommand {
    private final String uniqueKey;
    private final Integer threshold;

    public UpdateFeatureBattleThresholdCommand(
            @JsonProperty("id") String uniqueKey,
            @JsonProperty("threshold") Integer threshold){
        this.uniqueKey = uniqueKey;
        this.threshold = threshold;
    }
}
