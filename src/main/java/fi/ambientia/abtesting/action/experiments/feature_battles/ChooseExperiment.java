package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChooseExperiment {
    private final AlreadyDecidedBattles alreadyDecided;
    private final ExecuteFeatureBattle executeFeatureBattle;

    @Autowired
    public ChooseExperiment(AlreadyDecidedBattles alreadyDecided, ExecuteFeatureBattle executeFeatureBattle) {
        this.alreadyDecided = alreadyDecided;
        this.executeFeatureBattle = executeFeatureBattle;
    }

    public Experiment forUser(UserIdentifier user, ExperimentIdentifier experiment) {
        Optional<Experiment> experimentOptional = experimentForUser(user, experiment);

        return experimentOptional.orElse( executeFeatureBattleAndGetResult( user , experiment) );
    }

    private Experiment executeFeatureBattleAndGetResult(UserIdentifier user, ExperimentIdentifier experiment) {
        executeFeatureBattle.experimentOf(experiment).targetedFor(user );
        // TODO AkS: This might return null - if and only if the executeFeatureBattle does not work as expected
        return experimentForUser(user, experiment).get();
    }

    private Optional<Experiment> experimentForUser(UserIdentifier user, ExperimentIdentifier experiment) {
        return alreadyDecided.experimentOf(experiment).targetedFor(user);
    }
}
