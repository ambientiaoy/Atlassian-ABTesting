package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRandomizer;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface FeatureBattleRepository {
    List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier experiment);

    ExperimentRandomizer experimentRandomizer(FeatureBattleIdentifier featureBattleIdentifier);

    CreateNewFeatureBattleFor newFeatureBattleFor(FeatureBattleIdentifier experiment);

    List<Experiment> experimentsForUser(UserIdentifier userientifier);

    Optional<FeatureBattle> getFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier);

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
