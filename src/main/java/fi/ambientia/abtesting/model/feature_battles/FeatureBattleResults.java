package fi.ambientia.abtesting.model.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.user.UserIdentifier;

import java.util.List;
import java.util.function.Consumer;

public interface  FeatureBattleResults<T extends FeatureBattleEntity> {
    public List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier experiment)  ;

    AddNewFeatureBattleResult newWinnerFor(T featureBattleIdentifier);



    @FunctionalInterface
    interface  AddNewFeatureBattleResult {
        AndStoreResult forUser(UserIdentifier userIdentifier);
    }

    @FunctionalInterface
    interface AndStoreResult extends Consumer<Experiment.Type>{
        default void resultBeing(Experiment.Type type) {
            this.accept( type );
        }
    }
}
