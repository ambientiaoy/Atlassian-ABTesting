package fi.ambientia.atlassian.action;

import fi.ambientia.abtesting.infrastructure.repositories.AbTestInstanceRepository;
import fi.ambientia.abtesting.model.ABTestInstance;

public class AbTestHypothesis implements CreateHypothesis {
    private AbTestInstanceRepository abTestInstanceRepository;

    public AbTestHypothesis(AbTestInstanceRepository abTestInstanceRepository) {

        this.abTestInstanceRepository = abTestInstanceRepository;
    }

    public void createNew(ABTestInstance abTestInstance) {
        abTestInstanceRepository.createNew(abTestInstance);
    }
}
