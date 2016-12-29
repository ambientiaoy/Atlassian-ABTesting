package fi.ambientia.abtesting.infrastructure.repositories.persistence;

import fi.ambientia.abtesting.model.experiments.Experiment;
import net.java.ao.Entity;
import net.java.ao.OneToMany;
import net.java.ao.schema.Table;

@Table("FeatureBattles")
public interface FeatureBattleAO extends Entity{
    void setFeatureBattleId(String identifier);
    String getFeatureBattleId();

    void setThreshold(int threshold);
    int getThreshold();

    @OneToMany
    ExperimentAO[] getExperiments();
}
