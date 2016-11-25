package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.FeatureBattleService;

import java.io.Serializable;

public class DisplayFeatureBattleExperiment<T extends Serializable> {
    public DisplayFeatureBattleExperiment(FeatureBattleService featureBattleService) {

    }

    public Experiment displayContent(T userKey) {
        return null;
    }
}
