package fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;

import java.util.Random;
import java.util.function.BiFunction;

public interface Experiment {

    String ABTEST = "ABTEST";
    String INCLUDE_PAGE = "{include:%s:%s}";

    Type type();
    String render();

    boolean isRepresentedBy(FeatureBattleIdentifier featureBattleIdentifier);

    static WithIdentifier forType(Type experimentType) {
        return experimentType.equals( Type.NEW_AND_SHINY ) ?
                ( experimentIdentifier, page ) -> new NewAndShiny( experimentIdentifier, page) :
                ( experimentIdentifier, page ) -> new GoodOldWay( experimentIdentifier, page);
    }

    static Experiment.Type randomize(Random random, Integer threshold, FeatureBattleIdentifier featureBattleIdentifier) {
        return random.nextInt(100) < threshold ? Type.NEW_AND_SHINY : Type.GOOD_OLD;
    }

    static Experiment missingExperiment() {
        return new MissingExperiment();
    }

    enum Type {
        NEW_AND_SHINY,
        GOOD_OLD
    }

    @FunctionalInterface
    public interface WithIdentifier extends BiFunction<FeatureBattleIdentifier, PageObject, Experiment> {
        default Experiment withIdentifier(String identifier, PageObject page){
            return this.apply( new FeatureBattleIdentifier(identifier) , page);
        }
    }
}
