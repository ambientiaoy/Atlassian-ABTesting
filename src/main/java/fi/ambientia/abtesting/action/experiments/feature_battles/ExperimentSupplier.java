package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.Identifier;
import fi.ambientia.abtesting.model.experiments.Experiment;

import java.util.Optional;

@FunctionalInterface
public interface ExperimentSupplier {

    public Optional<Experiment> targetedFor(Identifier identifier);
}
