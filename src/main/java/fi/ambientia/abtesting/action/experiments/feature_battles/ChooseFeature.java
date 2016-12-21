package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
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

    public Experiment forUser(String userKey) {
        Optional<Experiment> experimentOptional = alreadyDecidedBattles.forIdentifier(userKey);

        return experimentOptional.orElse( executeFeatureBattleAndGetResult( userKey ) );
    }

    private Experiment executeFeatureBattleAndGetResult(String userKey) {
        executeFeatureBattle.forIdentifier(userKey );
        return alreadyDecidedBattles.forIdentifier( userKey ).get();
    }
}
