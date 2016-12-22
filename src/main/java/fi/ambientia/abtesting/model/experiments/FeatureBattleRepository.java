package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.Identifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;

import java.util.List;
import java.util.Optional;

public interface FeatureBattleRepository {
    Optional<Experiment> randomBattleResultFor(Identifier identifier);

    List<FeatureBattleResult> experimentsFor(ExperimentIdentifier experiment);

    ExperimentRandomizer experimentRandomizer(ExperimentIdentifier experimentIdentifier);
}
