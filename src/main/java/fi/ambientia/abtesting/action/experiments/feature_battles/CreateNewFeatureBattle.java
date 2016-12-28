package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.write.FeatureBattleInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateNewFeatureBattle implements CreateExperiment {
    private final FeatureBattleRepository featureBattleRepository;

    @Autowired
    public CreateNewFeatureBattle(FeatureBattleRepository featureBattleRepository) {
        this.featureBattleRepository = featureBattleRepository;
    }

    public void createNew(FeatureBattleInput featureBattleInput) {
        featureBattleRepository.createFeatureBattle(featureBattleInput.getFeatureBattleIdentifier(), "good old", "shiny new");
        featureBattleRepository.setThreshold( featureBattleInput.getFeatureBattleIdentifier(),  featureBattleInput.getThreshold() );
    }
}
