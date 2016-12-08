package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.FeatureBattleService;

import java.io.Serializable;

public class ChooseFeature<T extends Serializable> {
    public ChooseFeature(FeatureBattleService featureBattleService) {

    }

    public Experiment forUser(T userKey) {
        return null;
    }
}
