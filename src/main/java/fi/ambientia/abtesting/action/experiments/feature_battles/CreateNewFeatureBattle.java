package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateNewFeatureBattle implements CreateExperiment {
    private final FeatureBattleRepository featureBattleRepository;
    private final ExperimentRepository experimentRepository;

    @Autowired
    public CreateNewFeatureBattle(FeatureBattleRepository featureBattleRepository, ExperimentRepository experimentRepository) {
        this.featureBattleRepository = featureBattleRepository;
        this.experimentRepository = experimentRepository;
    }

    public void createNew(CreateNewFeatureBattleCommand featureBattleInput) {
        Integer featureBattleId = featureBattleRepository.createFeatureBattle(featureBattleInput.getFeatureBattleIdentifier()).andGetId();
        featureBattleRepository.setThreshold( featureBattleId,  featureBattleInput.getThreshold() );

        Integer goodOldId = experimentRepository.create(featureBattleId, Experiment.Type.GOOD_OLD).forPage(featureBattleInput.getGoodOldPage()).andGetId();
        Integer shinyNewId = experimentRepository.create(featureBattleId, Experiment.Type.NEW_AND_SHINY).forPage(featureBattleInput.getNewAndShinyPage()).andGetId();

    }
}
