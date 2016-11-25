package fi.ambientia.atlassian.routes.arguments;

public class JsonFeatureBattleArgument implements fi.ambientia.abtesting.model.write.FeatureBattle {
    private String uniqueKey;

    public JsonFeatureBattleArgument(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }
}
