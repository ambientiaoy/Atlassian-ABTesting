package fi.ambientia.abtesting.model.feature_battles;

import java.util.List;

public interface  FeatureBattleResults {
    public List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier experiment)  ;
}
