package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.infrastructure.repositories.AbTestInstanceRepository;
import fi.ambientia.abtesting.model.write.FeatureBattle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeatureBattleExperimentAction implements CreateExperiment {
    private AbTestInstanceRepository abTestInstanceRepository;

    @Autowired
    public FeatureBattleExperimentAction(AbTestInstanceRepository abTestInstanceRepository) {

        this.abTestInstanceRepository = abTestInstanceRepository;
    }



    public void createNew(FeatureBattle abTestInstance) {
        if (!abTestInstanceRepository.exists(abTestInstance)) {
            abTestInstanceRepository.createNew(abTestInstance);
        }
    }
}
