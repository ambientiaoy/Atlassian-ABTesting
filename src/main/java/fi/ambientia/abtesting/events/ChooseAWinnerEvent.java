package fi.ambientia.abtesting.events;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;

public class ChooseAWinnerEvent implements Event {
    private final UserIdentifier userIdentifier;
    private final FeatureBattleIdentifier featureBattleIdentifier;
    private final Experiment.Type type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChooseAWinnerEvent)) return false;

        ChooseAWinnerEvent that = (ChooseAWinnerEvent) o;

        if (userIdentifier != null ? !userIdentifier.equals(that.userIdentifier) : that.userIdentifier != null)
            return false;
        if (featureBattleIdentifier != null ? !featureBattleIdentifier.equals(that.featureBattleIdentifier) : that.featureBattleIdentifier != null)
            return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = userIdentifier != null ? userIdentifier.hashCode() : 0;
        result = 31 * result + (featureBattleIdentifier != null ? featureBattleIdentifier.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public ChooseAWinnerEvent(UserIdentifier userIdentifier, FeatureBattleIdentifier featureBattleIdentifier, Experiment.Type type) {
        this.userIdentifier = userIdentifier;
        this.featureBattleIdentifier = featureBattleIdentifier;
        this.type = type;
    }

    public Experiment.Type getType() {
        return type;
    }

    public FeatureBattleIdentifier getFeatureBattleIdentifier() {
        return featureBattleIdentifier;
    }

    public UserIdentifier getUserIdentifier() {
        return userIdentifier;
    }
}
