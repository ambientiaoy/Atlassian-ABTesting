package fi.ambientia.abtesting.model;

import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;

public class FeatureBattle {

    private final ExperimentIdentifier experimentIdentifier;

    public FeatureBattle(ExperimentIdentifier experimentIdentifier) {
        this.experimentIdentifier = experimentIdentifier;
    }
}
