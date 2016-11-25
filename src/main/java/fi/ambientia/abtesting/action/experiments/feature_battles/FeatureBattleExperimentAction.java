package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.infrastructure.repositories.AbTestInstanceRepository;
import fi.ambientia.abtesting.model.write.FeatureBattle;

public class FeatureBattleExperimentAction implements CreateExperiment {
    private AbTestInstanceRepository abTestInstanceRepository;

    public FeatureBattleExperimentAction(AbTestInstanceRepository abTestInstanceRepository) {

        this.abTestInstanceRepository = abTestInstanceRepository;
    }

    public void createNew(FeatureBattle abTestInstance) {
        abTestInstanceRepository.createNew(abTestInstance);
    }
}
