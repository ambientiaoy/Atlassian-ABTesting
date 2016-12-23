package fi.ambientia.abtesting.infrastructure.repositories.persistence;

import fi.ambientia.abtesting.model.experiments.Experiment;
import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("UserExperiment")
public interface UserExperimentAO extends Entity {

    void setExperimentId(String identifier);

    void setUserId(String identifier);

    void setExperiment(Experiment.Type type);
}
