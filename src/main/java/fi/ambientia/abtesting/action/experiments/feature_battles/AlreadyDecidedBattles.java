package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import fi.ambientia.abtesting.model.feature_battles.UserExperimentRepository;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AlreadyDecidedBattles {
    private final FeatureBattleResults featureBattleRepository;
    private final UserExperimentRepository userExperimentRepository;

    @Autowired
    public AlreadyDecidedBattles(FeatureBattleResults featureBattleRepository, UserExperimentRepository userExperimentRepository) {
        this.featureBattleRepository = featureBattleRepository;
        this.userExperimentRepository = userExperimentRepository;
    }

    public ExperimentSupplier experimentOf(FeatureBattleIdentifier experiment, UserIdentifier user) {
        List<FeatureBattleResult> featureBattleResults = featureBattleRepository.featureBattleResultsFor(experiment, user);
        List<FeatureBattleResult> userExperimentResults= userExperimentRepository.featureBattleResultsFor(experiment, user);

        ExperimentSupplier experimentSupplier = (predicate) -> {
            featureBattleResults.addAll(userExperimentResults);
            Optional<Experiment> first = featureBattleResults.
                    stream().
                    filter(predicate).
                    map(FeatureBattleResult::getExperiment).
                    findFirst();
            return first;
        };

        return experimentSupplier;
    }
}
