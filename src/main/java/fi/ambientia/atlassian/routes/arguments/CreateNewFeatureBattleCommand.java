package fi.ambientia.atlassian.routes.arguments;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.write.FeatureBattleInput;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class CreateNewFeatureBattleCommand {

    private final String uniqueKey;
    private final Integer threshold;
    private final String goodOldPage;
    private final String newAndShinyPage;

    @JsonCreator
    public CreateNewFeatureBattleCommand(
            @JsonProperty("id") String uniqueKey,
            @JsonProperty("threshold") Integer threshold,
            @JsonProperty("goodOld") String goodOldPage,
            @JsonProperty("newAndShiny") String newAndShinyPage) {
        this.uniqueKey = uniqueKey;
        this.threshold = threshold;
        this.goodOldPage = goodOldPage;
        this.newAndShinyPage = newAndShinyPage;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public FeatureBattleIdentifier getFeatureBattleIdentifier() {
        return new FeatureBattleIdentifier( uniqueKey );
    }

    public Integer getThreshold() {
        return threshold;
    }

    public String getGoodOldPage() {
        return goodOldPage;
    }

    public String getNewAndShinyPage() {
        return newAndShinyPage;
    }
}
