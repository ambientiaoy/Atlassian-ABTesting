package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.Optional;

@FunctionalInterface
public interface ExperimentSupplier {

    public Optional<Experiment> targetedFor(UserIdentifier identifier);
}
