package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.Identifier;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;

public class FeatureBattleResult {
    public boolean forUser(Identifier user) {
        return false;

    }

    public Experiment getExperiment() {
        return new NewAndShiny();
    }
}
