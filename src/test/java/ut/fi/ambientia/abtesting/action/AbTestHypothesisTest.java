package ut.fi.ambientia.abtesting.action;

import fi.ambientia.abtesting.infrastructure.repositories.AbTestInstanceRepository;
import fi.ambientia.abtesting.action.experiments.feature_battles.CreateNewFeatureBattle;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.atlassian.routes.arguments.JsonFeatureBattleArgument;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AbTestHypothesisTest {

    private CreateExperiment hypothesis;
    private JsonFeatureBattleArgument abTestInstance;
    private AbTestInstanceRepository abTestInstanceRepository;

    @Before
    public void setUp() throws Exception {
        abTestInstanceRepository = mock(AbTestInstanceRepository.class);
        hypothesis = new CreateNewFeatureBattle(abTestInstanceRepository);
        abTestInstance = new JsonFeatureBattleArgument("UNIQUE_KEY", 10);

    }



    @Test
    public void shouldCreateNewInstance() throws Exception {

        hypothesis.createNew( abTestInstance );


        verify( abTestInstanceRepository ).createNew( abTestInstance );
    }
}