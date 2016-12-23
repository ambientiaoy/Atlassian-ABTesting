package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.ExperimentRandomizer;
import fi.ambientia.abtesting.model.experiments.FeatureBattleRepository;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FeatureBattleAORepository implements FeatureBattleRepository{

    @Autowired
    public FeatureBattleAORepository(@ComponentImport ActiveObjects ao) {

    }

    @Override
    public Optional<Experiment> randomBattleResultFor(ExperimentIdentifier identifier) {
        return Optional.of( new NewAndShiny(identifier) );
    }

    @Override
    public List<FeatureBattleResult> experimentsFor(ExperimentIdentifier experiment) {
        // TODO AkS implement!
        return new ArrayList<>();
    }

    @Override
    public ExperimentRandomizer experimentRandomizer(ExperimentIdentifier experimentIdentifier) {
        return () -> new NewAndShiny(experimentIdentifier);
    }

    @Override
    public CreateNewFeatureBattleFor newFeatureBattleFor(ExperimentIdentifier experiment) {
        return ( user) -> {
            return ( featureBattle ) -> {};
        };
    }

    @Override
    public List<Experiment> randomizedExperimentsFor(UserIdentifier userientifier) {
        return new ArrayList<>();
    }
}
