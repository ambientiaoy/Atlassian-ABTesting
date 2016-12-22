package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.Identifier;

import java.util.Optional;

public interface FeatureBattleRepository {
    Optional<Experiment> randomBattleResultFor(Identifier identifier);
}
