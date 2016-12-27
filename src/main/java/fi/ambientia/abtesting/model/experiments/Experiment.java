package fi.ambientia.abtesting.model.experiments;

import java.util.Random;
import java.util.function.Function;

public interface Experiment {

    String ABTEST = "ABTEST";
    String INCLUDE_PAGE = "{include:%s:%s}";

    Type type();
    String render();

    boolean isRepresentedBy(FeatureBattleIdentifier featureBattleIdentifier);

    static WithIdentifier forType(Type experimentType) {
        return experimentType.equals( Type.NEW_AND_SHINY ) ?
                ( experimentIdentifier ) -> new NewAndShiny( experimentIdentifier ) :
                ( experimentIdentifier ) -> new GoodOldWay( experimentIdentifier );
    }

    static Experiment randomize(Random random, Integer threshold, FeatureBattleIdentifier featureBattleIdentifier) {
        return random.nextInt(100) < threshold ?
            new NewAndShiny(featureBattleIdentifier) : new GoodOldWay(featureBattleIdentifier);
    }

    enum Type {
        NEW_AND_SHINY,
        GOOD_OLD
    }

    @FunctionalInterface
    public interface WithIdentifier extends Function<FeatureBattleIdentifier, Experiment> {
        default Experiment withIdentifier(String identifier){
            return this.apply( new FeatureBattleIdentifier(identifier) );
        }
    }
}
