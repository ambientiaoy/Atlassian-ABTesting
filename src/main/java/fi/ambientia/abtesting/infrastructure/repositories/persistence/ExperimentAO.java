package fi.ambientia.abtesting.infrastructure.repositories.persistence;

import fi.ambientia.abtesting.model.experiments.Experiment;
import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("Experiments")
public interface ExperimentAO extends Entity {

    String getExperimentId();
    void setExperimentId(String identifier);

    void setThreshold(Integer integer);
    Integer getThreshold();

    Experiment.Type getExperimentType();
    void setExperimentType(Experiment.Type type);
}
