package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.FeatureBattleRepository;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ExecuteFeatureBattle {


    private final FeatureBattleRepository featureBattleRepository;

    public ExecuteFeatureBattle(FeatureBattleRepository featureBattleRepository) {
        this.featureBattleRepository = featureBattleRepository;
    }

    public void forIdentifier(UserIdentifier userkey) {

    }

    public StoreUserConsumer forExperiment(ExperimentIdentifier experiment) {
        Experiment randomExperiment = featureBattleRepository.experimentRandomizer(experiment).randomize();

        return (UserIdentifier user) -> {
            featureBattleRepository.newFeatureBattleFor(experiment).forUser(user).resultBeing(randomExperiment);
        };
    }

    public interface StoreUserConsumer extends Consumer<UserIdentifier> {
        default void andStoreResultToRepository(UserIdentifier user) {
            this.accept( user );
        }
    }
}
