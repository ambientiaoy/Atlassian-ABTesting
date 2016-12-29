package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Predicate;

@Component
public class ChooseExperiment {
    private final AlreadyDecidedBattles alreadyDecided;
    private final ExecuteFeatureBattle executeFeatureBattle;
    private final RandomizeFeatureBattle randomizeFeatureBattle;

    @Autowired
    public ChooseExperiment(AlreadyDecidedBattles alreadyDecided, ExecuteFeatureBattle executeFeatureBattle, RandomizeFeatureBattle randomizeFeatureBattle) {
        this.alreadyDecided = alreadyDecided;
        this.executeFeatureBattle = executeFeatureBattle;
        this.randomizeFeatureBattle = randomizeFeatureBattle;
    }

    public MathingFeatureBattleResult forFeatureBattle(UserIdentifier user, FeatureBattleIdentifier experiment) {

        return ( predicate ) ->
        {
            Optional<Experiment> experimentOptional = alreadyDecided.experimentOf(experiment).targetedFor(predicate);

            return experimentOptional.orElseGet(() -> executeFeatureBattleAndGetResult(user, experiment));
        };
    }

    private Experiment executeFeatureBattleAndGetResult(UserIdentifier user, FeatureBattleIdentifier featureBattleIdentifier) {
        executeFeatureBattle.forExperiment(featureBattleIdentifier).andStoreResultToRepository( user );

        return randomizeFeatureBattle.getExperiment(featureBattleIdentifier).forUser(user);
    }

    public static Predicate<FeatureBattleResult> forExperimentType(Experiment.Type type){
        return (featureBattleResult -> featureBattleResult.forExperimentType(type)) ;
    }

    public static Predicate<FeatureBattleResult> forUser(UserIdentifier currentUserIdentifier) {
        return (featureBattleResult) -> featureBattleResult.forUser( currentUserIdentifier );
    }


    @FunctionalInterface
    public interface MathingFeatureBattleResult{
        public Experiment matching(Predicate<FeatureBattleResult> predicate);
    }
}
