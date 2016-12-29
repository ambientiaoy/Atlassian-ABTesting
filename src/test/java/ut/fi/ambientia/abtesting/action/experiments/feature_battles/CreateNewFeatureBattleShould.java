package ut.fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.feature_battles.CreateNewFeatureBattle;
import fi.ambientia.abtesting.model.IdResolver;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;
import org.junit.Test;
import ut.fi.ambientia.abtesting.model.TestData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateNewFeatureBattleShould {

    public static final String PAGE_GOOD_OLD = "good old";
    public static final String PAGE_SHINY_NEW = "shiny new";

    @Test
    public void create_new_feature_battle() throws Exception {

        FeatureBattleRepository featureBattleRepository = mock(FeatureBattleRepository.class);
        ExperimentRepository experimentRepository = mock(ExperimentRepository.class);
        CreateNewFeatureBattle createNewFeatureBattle = new CreateNewFeatureBattle(featureBattleRepository, experimentRepository);

        String identifier = TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier();
        int threshold = 16;

        when(featureBattleRepository.createFeatureBattle( TestData.FEATURE_BATTLE_IDENTIFIER )).thenReturn(() -> 10);
        ExperimentRepository.CreateNewExperiment createGoodOld= mock(ExperimentRepository.CreateNewExperiment.class);
        ExperimentRepository.CreateNewExperiment createNewAndShiny = mock(ExperimentRepository.CreateNewExperiment.class);
        when( experimentRepository.create(10, Experiment.Type.GOOD_OLD)).thenReturn( createGoodOld );
        when( experimentRepository.create(10, Experiment.Type.NEW_AND_SHINY)).thenReturn( createNewAndShiny);
        when( createGoodOld.forPage(any())).thenReturn( () -> 1);
        when( createNewAndShiny.forPage(any())).thenReturn( () -> 1);

        createNewFeatureBattle.createNew( new CreateNewFeatureBattleCommand(identifier, threshold, PAGE_GOOD_OLD, PAGE_SHINY_NEW));

        verify( experimentRepository ).create(10, Experiment.Type.GOOD_OLD);
        verify( experimentRepository ).create(10, Experiment.Type.NEW_AND_SHINY);

        verify( createGoodOld ).forPage(PAGE_GOOD_OLD);
        verify( createNewAndShiny).forPage(PAGE_SHINY_NEW);
    }

}