package fi.ambientia.abtesting.infrastructure.repositories.persistence;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleEntity;
import net.java.ao.Entity;
import net.java.ao.OneToMany;
import net.java.ao.schema.Table;

@Table("FeatureBattles")
public interface FeatureBattleAO extends Entity, FeatureBattleEntity{
    void setFeatureBattleId(String identifier);

    void setThreshold(int threshold);
    int getThreshold();

    @OneToMany
    ExperimentAO[] getExperiments();
}
