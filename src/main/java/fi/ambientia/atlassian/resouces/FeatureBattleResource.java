package fi.ambientia.atlassian.resouces;

import org.codehaus.jackson.annotate.JsonProperty;

public class FeatureBattleResource {
    @JsonProperty private final String id;
    @JsonProperty private final int threshold;
    @JsonProperty private final String goodOld;
    @JsonProperty private final String newAndShiny;

    public FeatureBattleResource(String feature_battle_id, int threshold, String goodOld, String newAndShiny) {
        this.id = feature_battle_id;
        this.threshold = threshold;
        this.goodOld = goodOld;
        this.newAndShiny = newAndShiny;
    }
}
