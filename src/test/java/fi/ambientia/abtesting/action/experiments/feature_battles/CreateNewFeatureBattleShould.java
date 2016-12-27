package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.atlassian.routes.arguments.JsonFeatureBattleArgument;
import org.junit.Test;
import ut.fi.ambientia.abtesting.model.TestData;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateNewFeatureBattleShould {

    @Test
    public void create_new_feature_battle() throws Exception {

        FeatureBattleRepository featureBattleRepository = mock(FeatureBattleRepository.class);
        CreateNewFeatureBattle createNewFeatureBattle = new CreateNewFeatureBattle(featureBattleRepository);

        String identifier = TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier();
        int threshold = 16;
        createNewFeatureBattle.createNew( new JsonFeatureBattleArgument(identifier, threshold));

        verify( featureBattleRepository ).createFeatureBattle( TestData.FEATURE_BATTLE_IDENTIFIER );
        verify( featureBattleRepository ).setThreshold( TestData.FEATURE_BATTLE_IDENTIFIER, threshold);
    }
}