package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.IdResolver;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRandomizer;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface FeatureBattleRepository {
    IdResolver createFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier);

    void setThreshold(Integer featureBattleIdentifier, int threshold);

    default ExperimentRandomizer experimentRandomizer(FeatureBattleIdentifier featureBattleIdentifier) {
        return () -> randomBattleResultFor(featureBattleIdentifier);
    }
    Experiment randomBattleResultFor(FeatureBattleIdentifier featureBattleIdentifier);


    CreateNewFeatureBattleFor newFeatureBattleFor(FeatureBattleIdentifier experiment);

    Optional<FeatureBattle> getFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier);

    List<FeatureBattle> getAll();

    @FunctionalInterface
    interface NewFeatureBattleForOldPage{
        // TODO AkS: Add a wrapping concept for a page
        NewFeatureBattleForNewPage forOldPage( String pagePath );
    }
    @FunctionalInterface
    interface NewFeatureBattleForNewPage{
        void forNewPage( String string);
    }



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

    @FunctionalInterface
    interface FeatureBattleResolver {
        FeatureBattle andGetId();
    }
}
