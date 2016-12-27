package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

public class FeatureBattle {

    private final FeatureBattleIdentifier featureBattleIdentifier;

    public FeatureBattle(FeatureBattleIdentifier featureBattleIdentifier) {
        this.featureBattleIdentifier = featureBattleIdentifier;
    }

    @JsonValue
    public String getId() {
        return featureBattleIdentifier.getIdentifier();
    }

    @JsonValue
    public Integer getThreshold(){
        return 10;
    }

    @JsonValue
    public String getGooldOld(){
        return "Good Old";
    }

    @JsonValue
    public String getNewAndShiny(){
        return "New and shiny";
    }

    public FeatureBattleIdentifier getIdentifier() {
        return featureBattleIdentifier;
    }
}
