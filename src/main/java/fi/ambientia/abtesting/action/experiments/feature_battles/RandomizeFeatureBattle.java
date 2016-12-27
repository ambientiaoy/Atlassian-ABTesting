package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class RandomizeFeatureBattle {

    private final FeatureBattleRepository featureBattleRepository;
    private final ExperimentRepository experimentRepository;

    @Autowired
    public RandomizeFeatureBattle(FeatureBattleRepository featureBattleRepository, ExperimentRepository experimentRepository) {
        this.featureBattleRepository = featureBattleRepository;
        this.experimentRepository = experimentRepository;
    }

    public GetExperimentForUser getExperiment(FeatureBattleIdentifier featureBattleIdentifier) {
        // TODO AkS: execute this from DB?
        return (user) -> {
            Optional<Experiment> first = experimentRepository.experimentsForUser(user).stream().
                    filter(experiment -> experiment.isRepresentedBy(featureBattleIdentifier)).
                    findFirst();

            return first.orElse( featureBattleRepository.experimentRandomizer(featureBattleIdentifier).randomize() );
        };
    }

    public interface GetExperimentForUser extends Function<UserIdentifier, Experiment>{

        default Experiment forUser(UserIdentifier user) {
            return this.apply(user);
        }
    }
}
