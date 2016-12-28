package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.infrastructure.repositories.ExperimentAORepository;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ExecuteFeatureBattle {

    private final FeatureBattleRepository featureBattleRepository;
    private final ExperimentRepository experimentRepository;

    @Autowired
    public ExecuteFeatureBattle(FeatureBattleRepository featureBattleRepository, ExperimentRepository experimentRepository) {
        this.featureBattleRepository = featureBattleRepository;
        this.experimentRepository = experimentRepository;
    }

    public StoreUserConsumer forExperiment(FeatureBattleIdentifier featureBattleIdentifier) {
        Experiment randomExperiment = featureBattleRepository.experimentRandomizer(featureBattleIdentifier).randomize();

        return (UserIdentifier user) -> {
            featureBattleRepository.newFeatureBattleFor(featureBattleIdentifier).forUser(user).resultBeing(randomExperiment);
        };
    }

    public interface StoreUserConsumer extends Consumer<UserIdentifier> {
        default void andStoreResultToRepository(UserIdentifier user) {
            this.accept( user );
        }
    }
}
