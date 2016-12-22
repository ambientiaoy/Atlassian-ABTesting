package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.Identifier;

public class ExperimentIdentifier implements Identifier {
    private final String experiment_id;

    public ExperimentIdentifier(String experiment_id) {
        this.experiment_id = experiment_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExperimentIdentifier)) return false;

        ExperimentIdentifier that = (ExperimentIdentifier) o;

        return experiment_id != null ? experiment_id.equals(that.experiment_id) : that.experiment_id == null;

    }

    @Override
    public int hashCode() {
        return experiment_id != null ? experiment_id.hashCode() : 0;
    }

    @Override
    public String getIdentifier() {
        return experiment_id;
    }
}
