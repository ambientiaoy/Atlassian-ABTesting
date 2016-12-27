package ut.fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.action.experiments.feature_battles.ExecuteFeatureBattle;
import fi.ambientia.abtesting.model.experiments.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.experiments.FeatureBattleRepository;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExecuteFeatureBattleShould {


    private static final FeatureBattleIdentifier EXPERIMENT_IDENTIFIER = new FeatureBattleIdentifier("FOOBAR");
    private static final UserIdentifier USER_IDENTIFIER = new UserIdentifier("BARFOO");
    private ExecuteFeatureBattle executeFeatureBattle;
    private FeatureBattleRepository featureBattleRepository;

    @Test
    public void find_feature_battle_details_from_repository() throws Exception {
        GoodOldWay randomExperiment = new GoodOldWay(EXPERIMENT_IDENTIFIER);
        featureBattleRepository = mock(FeatureBattleRepository.class);
        executeFeatureBattle = new ExecuteFeatureBattle(featureBattleRepository);
        FeatureBattleRepository.StoreExperiment mock = mock(FeatureBattleRepository.StoreExperiment.class);

        when(featureBattleRepository.experimentRandomizer(EXPERIMENT_IDENTIFIER)).thenReturn(() -> randomExperiment);

        when(featureBattleRepository.newFeatureBattleFor( EXPERIMENT_IDENTIFIER )).thenReturn(
                (user) -> {
                    return mock;
                });

        executeFeatureBattle.
                forExperiment(EXPERIMENT_IDENTIFIER).
                andStoreResultToRepository( USER_IDENTIFIER );

        verify(mock).resultBeing( randomExperiment );
    }


}