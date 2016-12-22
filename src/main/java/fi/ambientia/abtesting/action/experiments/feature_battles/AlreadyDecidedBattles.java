package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.Identifier;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.FeatureBattleRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AlreadyDecidedBattles {
    private final FeatureBattleRepository featureBattleRepository;

    public AlreadyDecidedBattles(FeatureBattleRepository featureBattleRepository) {
        this.featureBattleRepository = featureBattleRepository;
    }

    public Optional<Experiment> forIdentifier(Identifier identifier) {
        Optional<Experiment> experimentOptional = featureBattleRepository.randomBattleResultFor(identifier);

        return experimentOptional;
    }

    public ExperimentSupplier experimentOf(ExperimentIdentifier experiment) {
        List<FeatureBattleResult> featureBattleResults = featureBattleRepository.experimentsFor(experiment);

        return (user) -> featureBattleResults.
                stream().
                filter(featureBattleResult -> featureBattleResult.forUser( user )).
                map(FeatureBattleResult::getExperiment).
                findFirst();
    }
}
