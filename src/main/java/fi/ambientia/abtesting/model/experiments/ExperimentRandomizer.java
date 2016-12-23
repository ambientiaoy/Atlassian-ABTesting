package fi.ambientia.abtesting.model.experiments;

import java.util.function.Supplier;

@FunctionalInterface
public interface ExperimentRandomizer extends Supplier<Experiment> {

    default public Experiment randomize() {
        return this.get();
    }
}
