package fi.ambientia.abtesting.infrastructure.repositories.persistence;

import fi.ambientia.abtesting.model.experiments.Experiment;
import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("UserExperiment")
public interface UserExperimentAO extends Entity {

    String getUserId();
    void setUserId(String identifier);

    ExperimentAO getExperiment();
    void setExperiment(ExperimentAO experiment);

    Experiment.Type getExperimentType();
    void setExperimentType(Experiment.Type type);
}
