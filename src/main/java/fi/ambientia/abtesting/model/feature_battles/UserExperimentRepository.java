package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.List;

public interface UserExperimentRepository {
    List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier experimentIdentifier, UserIdentifier user);
}
