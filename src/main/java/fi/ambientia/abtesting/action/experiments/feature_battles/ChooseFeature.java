package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChooseFeature {
    private final AlreadyDecidedBattles alreadyDecide;
    private final ExecuteFeatureBattle executeFeatureBattle;

    @Autowired
    public ChooseFeature(AlreadyDecidedBattles alreadyDecide, ExecuteFeatureBattle executeFeatureBattle) {
        this.alreadyDecide = alreadyDecide;
        this.executeFeatureBattle = executeFeatureBattle;
    }

    public Experiment forUser(UserIdentifier user, ExperimentIdentifier experiment) {
        Optional<Experiment> experimentOptional = alreadyDecide.forIdentifier(user);
//        Optional<Experiment> experimentOptional = alreadyDecide.experimentOf(experiment).targetedFor(user);

        return experimentOptional.orElse( executeFeatureBattleAndGetResult( user ) );
    }

    private Experiment executeFeatureBattleAndGetResult(UserIdentifier userKey) {
        executeFeatureBattle.forIdentifier(userKey );
        // TODO AkS: This might return null - if and only if the executeFeatureBattle does not work as expected
        return alreadyDecide.forIdentifier( userKey ).get();
    }
}
