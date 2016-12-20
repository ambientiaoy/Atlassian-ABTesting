package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.AlreadyDecidedBattles;

import java.util.Optional;

public class ChooseFeature {
    private final AlreadyDecidedBattles alreadyDecidedBattles;
    private final ExecuteFeatureBattle executeFeatureBattle;

    public ChooseFeature(AlreadyDecidedBattles alreadyDecidedBattles, ExecuteFeatureBattle executeFeatureBattle) {
        this.alreadyDecidedBattles = alreadyDecidedBattles;
        this.executeFeatureBattle = executeFeatureBattle;
    }

    public Experiment forUser(String userKey) {
        Optional<Experiment> experimentOptional = alreadyDecidedBattles.forIdentifier(userKey);

        return experimentOptional.orElse( executeFeatureBattleAndGetResult( userKey ) );
    }

    private Experiment executeFeatureBattleAndGetResult(String userKey) {
        executeFeatureBattle.forIdentifier(userKey );
        return alreadyDecidedBattles.forIdentifier( userKey ).get();
    }
}
