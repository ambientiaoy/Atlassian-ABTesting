package fi.ambientia.abtesting.action;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ChooseAWinnerOfAFeatureBattle {
    public Consumer<Experiment.Type> forFeatureBattle(UserIdentifier userIdentifier, FeatureBattleIdentifier featureBattleIdentifier) {
        return ( experimentType ) -> {};
    }
}
