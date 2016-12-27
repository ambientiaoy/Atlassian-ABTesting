package ut.fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.feature_battles.CreateNewFeatureBattle;
import fi.ambientia.abtesting.infrastructure.repositories.AbTestInstanceRepository;
import fi.ambientia.abtesting.model.write.FeatureBattleInput;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class FeatureBattleExperimentActionShould {

    private AbTestInstanceRepository abTestInstanceRepository;
    private CreateNewFeatureBattle featureBattleExperimentAction;
    private FeatureBattleInput featureBattle;

    @Before
    public void setUp() throws Exception {
        abTestInstanceRepository = mock(AbTestInstanceRepository.class);
        featureBattleExperimentAction = new CreateNewFeatureBattle(abTestInstanceRepository);
        featureBattle = newFeatureBattle();
    }

    @Test
    public void create_new_feature_battle() throws Exception {
        featureBattleExperimentAction.createNew(featureBattle);

        verify(abTestInstanceRepository).createNew( featureBattle ) ;
    }

    @Test
    public void not_create_feature_battle_if_id_is_defined() throws Exception {
        when(abTestInstanceRepository.exists(featureBattle) ).thenReturn( true );

        featureBattleExperimentAction.createNew(featureBattle);

        verify(abTestInstanceRepository, never() ).createNew( featureBattle ) ;
    }


    private FeatureBattleInput newFeatureBattle() {
        return mock(FeatureBattleInput.class);
    }
}