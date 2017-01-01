package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import org.codehaus.jackson.annotate.JsonValue;

import java.util.List;
import java.util.function.Predicate;

public class FeatureBattle {

    private final FeatureBattleIdentifier featureBattleIdentifier;
    private final List<Experiment> experiments;

    public FeatureBattle(FeatureBattleIdentifier featureBattleIdentifier, List<Experiment> experiments) {
        this.featureBattleIdentifier = featureBattleIdentifier;
        this.experiments = experiments;
    }

    @JsonValue
    public String getId() {
        return featureBattleIdentifier.getFeatureBattleId();
    }

    @JsonValue
    public Integer getThreshold(){
        return 10;
    }

    @JsonValue
    public String getGooldOld(){
        return getPage(it -> it.type().equals(Experiment.Type.GOOD_OLD));
    }

    @JsonValue
    public String getNewAndShiny(){
        return getPage(it -> it.type().equals(Experiment.Type.NEW_AND_SHINY));
    }

    protected String getPage(Predicate<Experiment> predicate) {
        return experiments.stream().filter(predicate).map(it-> it.page()).findFirst().orElse("");
    }

    public FeatureBattleIdentifier getIdentifier() {
        return featureBattleIdentifier;
    }
}
