package ut.fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.feature_battles.ExecuteFeatureBattle;
import fi.ambientia.abtesting.model.Identifier;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.FeatureBattleRepository;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExecuteFeatureBattleShould {


    private static final ExperimentIdentifier EXPERIMENT_IDENTIFIER = new ExperimentIdentifier("FOOBAR");
    private static final UserIdentifier USER_IDENTIFIER = new UserIdentifier("BARFOO");
    private ExecuteFeatureBattle executeFeatureBattle;
    private FeatureBattleRepository featureBattleRepository;

    @Test
    public void find_feature_battle_details_from_repository() throws Exception {
        featureBattleRepository = mock(FeatureBattleRepository.class);
        executeFeatureBattle = new ExecuteFeatureBattle(featureBattleRepository);

        when(featureBattleRepository.experimentRandomizer(EXPERIMENT_IDENTIFIER)).thenReturn(() -> new GoodOldWay() );
        
        executeFeatureBattle.experimentOf( EXPERIMENT_IDENTIFIER ).targetedFor( USER_IDENTIFIER );
    }


}