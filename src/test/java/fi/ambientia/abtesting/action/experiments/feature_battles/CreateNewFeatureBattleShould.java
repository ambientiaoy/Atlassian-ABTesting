package fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.atlassian.routes.arguments.CreateNewFeatureBattleCommand;
import org.junit.Test;
import ut.fi.ambientia.abtesting.model.TestData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateNewFeatureBattleShould {

    public static final String PAGE_GOOD_OLD = "good old";
    public static final String PAGE_SHINY_NEW = "shiny new";

    @Test
    public void create_new_feature_battle() throws Exception {

        FeatureBattleRepository featureBattleRepository = mock(FeatureBattleRepository.class);
        CreateNewFeatureBattle createNewFeatureBattle = new CreateNewFeatureBattle(featureBattleRepository);

        String identifier = TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier();
        int threshold = 16;
//        when( featureBattleRepository.createFeatureBattle( TestData.FEATURE_BATTLE_IDENTIFIER ) ).thenReturn(
//                (goodOldPage) -> (shinyNewPage ) -> {
//                    assertThat( goodOldPage, equalTo( PAGE_GOOD_OLD) );
//                    assertThat( shinyNewPage, equalTo( PAGE_SHINY_NEW ));
//                });

        createNewFeatureBattle.createNew( new CreateNewFeatureBattleCommand(identifier, threshold, PAGE_GOOD_OLD, PAGE_SHINY_NEW));

        verify( featureBattleRepository ).createFeatureBattle( TestData.FEATURE_BATTLE_IDENTIFIER, PAGE_GOOD_OLD, PAGE_SHINY_NEW);
        verify( featureBattleRepository ).setThreshold( TestData.FEATURE_BATTLE_IDENTIFIER, threshold);
    }

}