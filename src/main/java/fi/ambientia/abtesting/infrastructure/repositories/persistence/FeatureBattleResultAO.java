package fi.ambientia.abtesting.infrastructure.repositories.persistence;

import com.atlassian.elasticsearch.lucene.analysis.miscellaneous.WordDelimiterIterator;
import fi.ambientia.abtesting.model.experiments.Experiment;
import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("FB_Results")
public interface FeatureBattleResultAO extends Entity{

    FeatureBattleAO getFeatureBattle();
    void setFeatureBattle(FeatureBattleAO featureBattleAO);

    String getUserIdentifier();
    void setUserIdentifier(String userIdentifier);

    ExperimentAO getExperiment();
    void setExperiment(ExperimentAO experimentAO);
}
