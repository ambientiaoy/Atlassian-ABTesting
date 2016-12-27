package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRandomizer;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface FeatureBattleRepository {
    void createFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier);

    void setThreshold(FeatureBattleIdentifier featureBattleIdentifier, int threshold);

    default ExperimentRandomizer experimentRandomizer(FeatureBattleIdentifier featureBattleIdentifier) {
        return () -> randomBattleResultFor(featureBattleIdentifier);
    }
    Experiment randomBattleResultFor(FeatureBattleIdentifier featureBattleIdentifier);


    List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier experiment);

    CreateNewFeatureBattleFor newFeatureBattleFor(FeatureBattleIdentifier experiment);

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
