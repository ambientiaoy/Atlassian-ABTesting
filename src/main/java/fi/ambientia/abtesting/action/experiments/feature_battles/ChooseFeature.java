package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChooseFeature {
    private final AlreadyDecidedBattles alreadyDecidedBattles;
    private final ExecuteFeatureBattle executeFeatureBattle;

    @Autowired
    public ChooseFeature(AlreadyDecidedBattles alreadyDecidedBattles, ExecuteFeatureBattle executeFeatureBattle) {
        this.alreadyDecidedBattles = alreadyDecidedBattles;
        this.executeFeatureBattle = executeFeatureBattle;
    }

    public Experiment forUser(UserIdentifier userKey, ExperimentIdentifier identifier) {
        Optional<Experiment> experimentOptional = alreadyDecidedBattles.forIdentifier(userKey.getIdentifier());

        return experimentOptional.orElse( executeFeatureBattleAndGetResult( userKey.getIdentifier() ) );
    }

    private Experiment executeFeatureBattleAndGetResult(String userKey) {
        executeFeatureBattle.forIdentifier(userKey );
        // TODO AkS: This might return null - if and only if the executeFeatureBattle does not work as expected
        return alreadyDecidedBattles.forIdentifier( userKey ).get();
    }
}
