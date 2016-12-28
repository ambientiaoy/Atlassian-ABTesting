package fi.ambientia.abtesting.infrastructure.repositories.persistence;

import fi.ambientia.abtesting.model.experiments.Experiment;
import net.java.ao.Entity;
import net.java.ao.OneToMany;
import net.java.ao.schema.Table;

@Table("Experiments")
public interface ExperimentAO extends Entity {

    String  getExperimentId();
    void setExperimentId(String identifier);

    Experiment.Type getExperimentType();
    void setExperimentType(Experiment.Type type);

    void setPage(String page);
    String getPage();

    @OneToMany
    UserExperimentAO[] getUserExperiments();

    void setFeatureBattle(FeatureBattleAO featureBattleAO);
    FeatureBattleAO getFeatureBattle();
}
