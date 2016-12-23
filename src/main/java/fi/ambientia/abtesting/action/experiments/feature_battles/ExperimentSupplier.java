package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface ExperimentSupplier extends Function< UserIdentifier, Optional<Experiment>> {

    default Optional<Experiment> targetedFor(UserIdentifier identifier) {
        return this.apply(identifier);
    }
}
