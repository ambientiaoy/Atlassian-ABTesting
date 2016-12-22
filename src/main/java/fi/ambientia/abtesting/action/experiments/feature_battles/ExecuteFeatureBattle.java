package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.FeatureBattleRepository;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.elasticsearch.common.cli.commons.Option;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExecuteFeatureBattle {


    private final FeatureBattleRepository featureBattleRepository;

    public ExecuteFeatureBattle(FeatureBattleRepository featureBattleRepository) {
        this.featureBattleRepository = featureBattleRepository;
    }

    public void forIdentifier(UserIdentifier userkey) {

    }

    public ExperimentSupplier experimentOf(ExperimentIdentifier experiment) {
        Experiment randomExperiment = featureBattleRepository.experimentRandomizer(experiment).randomize();
        return ( user ) -> Optional.of(randomExperiment);
    }
}
