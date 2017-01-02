package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.user.TargetedForUserPredicate;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface ExperimentSupplier extends Function< Predicate<FeatureBattleResult>, Optional<Experiment>> {

    default Optional<Experiment> targetedFor(Predicate<FeatureBattleResult> identifier) {
        return this.apply(identifier);
    }
}
