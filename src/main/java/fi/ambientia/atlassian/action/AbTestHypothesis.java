package fi.ambientia.atlassian.action;

import fi.ambientia.abtesting.repository.AbTestInstanceRepository;
import fi.ambientia.atlassian.model.ABTestInstance;

public class AbTestHypothesis implements CreateHypothesis {
    private AbTestInstanceRepository abTestInstanceRepository;

    public AbTestHypothesis(AbTestInstanceRepository abTestInstanceRepository) {

        this.abTestInstanceRepository = abTestInstanceRepository;
    }

    public void createNew(ABTestInstance abTestInstance) {
        abTestInstanceRepository.createNew(abTestInstance);
    }
}
