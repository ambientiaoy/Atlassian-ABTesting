package fi.ambientia.atlassian.routes.arguments;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.write.FeatureBattleInput;

public class JsonFeatureBattleArgument implements FeatureBattleInput {
    private final String uniqueKey;
    private final Integer threshold;

    public JsonFeatureBattleArgument(String uniqueKey, Integer threshold) {
        this.uniqueKey = uniqueKey;
        this.threshold = threshold;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public FeatureBattleIdentifier getFeatureBattleIdentifier() {
        return new FeatureBattleIdentifier( uniqueKey );
    }

    @Override
    public Integer getThreshold() {
        return threshold;
    }
}
