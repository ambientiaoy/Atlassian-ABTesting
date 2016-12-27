package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;

import java.util.Random;
import java.util.function.Function;

public interface Experiment {

    String ABTEST = "ABTEST";
    String INCLUDE_PAGE = "{include:%s:%s}";

    Type type();
    String render();

    boolean isRepresentedBy(ExperimentIdentifier experimentIdentifier);

    static WithIdentifier forType(Type experimentType) {
        return experimentType.equals( Type.NEW_AND_SHINY ) ?
                ( experimentIdentifier ) -> new NewAndShiny( experimentIdentifier ) :
                ( experimentIdentifier ) -> new GoodOldWay( experimentIdentifier );
    }

    static Experiment randomize(Random random, Integer threshold, ExperimentIdentifier experimentIdentifier) {
        return random.nextInt(100) < threshold ?
            new NewAndShiny(experimentIdentifier) : new GoodOldWay( experimentIdentifier);
    }

    enum Type {
        NEW_AND_SHINY,
        GOOD_OLD
    }

    @FunctionalInterface
    public interface WithIdentifier extends Function<ExperimentIdentifier, Experiment> {
        default Experiment withIdentifier(String identifier){
            return this.apply( new ExperimentIdentifier(identifier) );
        }
    }
}
