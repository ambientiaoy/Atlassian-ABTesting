package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.Identifier;

public class ExperimentIdentifier implements Identifier {
    private final String experiment_id;

    public ExperimentIdentifier(String experiment_id) {
        this.experiment_id = experiment_id;
    }

    @Override
    public String getIdentifier() {
        return experiment_id;
    }
}
