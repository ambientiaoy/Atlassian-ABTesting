package fi.ambientia.abtesting.action.experiments;

import fi.ambientia.abtesting.model.write.FeatureBattleInput;

public interface CreateExperiment {
    public void createNew(FeatureBattleInput abTestInstance);
}
