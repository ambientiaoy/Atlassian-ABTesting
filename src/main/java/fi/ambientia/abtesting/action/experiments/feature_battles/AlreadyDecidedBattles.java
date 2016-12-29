package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlreadyDecidedBattles {
    private final FeatureBattleResults featureBattleRepository;

    @Autowired
    public AlreadyDecidedBattles(FeatureBattleResults featureBattleRepository) {
        this.featureBattleRepository = featureBattleRepository;
    }

    public ExperimentSupplier experimentOf(FeatureBattleIdentifier experiment) {
        List<FeatureBattleResult> featureBattleResults = featureBattleRepository.featureBattleResultsFor(experiment);

        return (predicate) -> featureBattleResults.
                stream().
                filter( predicate ).
                map(FeatureBattleResult::getExperiment).
                findFirst();
    }
}
