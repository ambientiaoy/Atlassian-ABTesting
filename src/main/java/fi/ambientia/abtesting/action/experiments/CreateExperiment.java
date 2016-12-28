package fi.ambientia.abtesting.action.experiments;

import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;

public interface CreateExperiment {
    public void createNew(CreateNewFeatureBattleCommand abTestInstance);
}
