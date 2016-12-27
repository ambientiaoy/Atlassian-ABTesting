package fi.ambientia.atlassian.routes.arguments;

public class JsonFeatureBattleArgument implements fi.ambientia.abtesting.model.write.FeatureBattle {
    private final String uniqueKey;
    private final Integer defaultThreshold;

    public JsonFeatureBattleArgument(String uniqueKey, Integer defaultThreshold) {
        this.uniqueKey = uniqueKey;
        this.defaultThreshold = defaultThreshold;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public Integer getDefaultThreshold() {
        return defaultThreshold;
    }
}
