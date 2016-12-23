package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.Identifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface FeatureBattleRepository {
    Optional<Experiment> randomBattleResultFor(Identifier identifier);

    List<FeatureBattleResult> experimentsFor(ExperimentIdentifier experiment);

    ExperimentRandomizer experimentRandomizer(ExperimentIdentifier experimentIdentifier);

    CreateNewFeatureBattleFor newFeatureBattleFor(ExperimentIdentifier experiment);

    @FunctionalInterface
    interface CreateNewFeatureBattleFor {

        StoreExperiment forUser(UserIdentifier user);
    }

    @FunctionalInterface
    interface StoreExperiment extends Consumer<Experiment> {
        default void resultBeing(Experiment randomExperiment) {
            this.accept( randomExperiment );
        }
    }
}
