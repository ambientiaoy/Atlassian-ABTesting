package ut.fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import fi.ambientia.abtesting.action.experiments.feature_battles.DisplayFeatureBattleExperiment;
import fi.ambientia.atlassian.macro.experiments.DisplayFeatureBattle;
import fi.ambientia.atlassian.users.MapCurrentUserToUserkey;
import org.junit.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

public class DisplayFeatureBattleShould {


    public static final String USER_KEY = "USER_KEY";
    private Map<String, String> map;
    private String string;
    private ConversionContext conversionContext;

    @Test
    public void map_to_user() throws Exception {

        MapCurrentUserToUserkey mapCurrentUserToUserkey = mock(MapCurrentUserToUserkey.class);
        when(mapCurrentUserToUserkey.getCurrentUserIdentifier()).thenReturn(USER_KEY);
        DisplayFeatureBattleExperiment displayFeatureBattleExperiment = mock(DisplayFeatureBattleExperiment.class);

        DisplayFeatureBattle displayFeatureBattle = new DisplayFeatureBattle(mapCurrentUserToUserkey, displayFeatureBattleExperiment);


        displayFeatureBattle.execute(map, string, conversionContext);

        verify( displayFeatureBattleExperiment).displayContent( USER_KEY );


    }
}