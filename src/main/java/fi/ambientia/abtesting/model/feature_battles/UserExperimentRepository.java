package fi.ambientia.abtesting.model.feature_battles;

import java.util.List;

public interface UserExperimentRepository {
    List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier experimentIdentifier);
}
