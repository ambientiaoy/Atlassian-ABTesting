package fi.ambientia.abtesting.action.experiments;

import fi.ambientia.abtesting.model.write.FeatureBattle;

public interface CreateExperiment {
    public void createNew(FeatureBattle abTestInstance);
}
