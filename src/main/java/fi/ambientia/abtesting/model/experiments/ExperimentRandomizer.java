package fi.ambientia.abtesting.model.experiments;

@FunctionalInterface
public interface ExperimentRandomizer {

    public Experiment randomize( );
}
