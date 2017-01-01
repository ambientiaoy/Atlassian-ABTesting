package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.Identifier;
import org.codehaus.jackson.annotate.JsonProperty;

public class FeatureBattleIdentifier implements FeatureBattleEntity {
    public static final String DEFAULT_IDENTIFIER = "DEFAULT IDENTIFIER FOR EXPERIMENTS";
    @JsonProperty
    private final String experiment_id;

    public FeatureBattleIdentifier(String experiment_id) {
        this.experiment_id = experiment_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureBattleIdentifier)) return false;

        FeatureBattleIdentifier that = (FeatureBattleIdentifier) o;

        return experiment_id != null ? experiment_id.equals(that.experiment_id) : that.experiment_id == null;

    }

    @Override
    public int hashCode() {
        return experiment_id != null ? experiment_id.hashCode() : 0;
    }

    @Override
    public String getFeatureBattleId() {
        return experiment_id;
    }

}
