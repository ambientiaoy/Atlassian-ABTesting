package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    public Experiment forUser(UserIdentifier user, FeatureBattleIdentifier experiment) {
        Optional<Experiment> experimentOptional = alreadyDecided.experimentOf(experiment).targetedFor(user);

        return experimentOptional.orElse( executeFeatureBattleAndGetResult( user , experiment) );
    }

    private Experiment executeFeatureBattleAndGetResult(UserIdentifier user, FeatureBattleIdentifier experiment) {
        executeFeatureBattle.forExperiment(experiment).andStoreResultToRepository( user );

        return randomizeFeatureBattle.getExperiment(experiment).forUser(user);
    }

}
