package ut.fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import fi.ambientia.abtesting.action.experiments.feature_battles.DisplayFeatureBattleExperiment;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.atlassian.macro.experiments.DisplayFeatureBattle;
import fi.ambientia.atlassian.users.MapCurrentUserToUserkey;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DisplayFeatureBattleShould {


    public static final String USER_KEY = "USER_KEY";
    private Map<String, String> map;
    private String string;
    private ConversionContext conversionContext;
    private MapCurrentUserToUserkey mapCurrentUserToUserkey;
    private SoyTemplateRenderer renderer;
    private DisplayFeatureBattleExperiment displayFeatureBattleExperiment;
    private DisplayFeatureBattle displayFeatureBattle;
    private Experiment experiment;

    @Before
    public void setUp() throws Exception {
        mapCurrentUserToUserkey = mock(MapCurrentUserToUserkey.class);
        displayFeatureBattleExperiment = mock(DisplayFeatureBattleExperiment.class);
        renderer = new SoyTemplateRendererStub();
        experiment = new GoodOldWay();

        displayFeatureBattle = new DisplayFeatureBattle(renderer, mapCurrentUserToUserkey, displayFeatureBattleExperiment);
    }

    @Test
    public void map_to_user() throws Exception {
        when(mapCurrentUserToUserkey.getCurrentUserIdentifier()).thenReturn(USER_KEY);
        when(displayFeatureBattleExperiment.displayContent( USER_KEY )).thenReturn( experiment );

        String execute = displayFeatureBattle.execute(map, string, conversionContext);

        assertThat(execute, equalTo("RENDERING fi.ambientia.abtesting.model.experiments.GoodOldWay"));
    }



    private class SoyTemplateRendererStub implements SoyTemplateRenderer {
        public void clearAllCaches() {

        }

        public void clearCache(String completeModuleKey) {

        }

        public String render(String completeModuleKey, String templateName, Map<String, Object> data) throws SoyException {
            return "RENDERING " + data.get("macroDef").getClass().getName();
        }

        public void render(Appendable appendable, String completeModuleKey, String templateName, Map<String, Object> data) throws SoyException {

        }

        public void render(Appendable appendable, String completeModuleKey, String templateName, Map<String, Object> data, Map<String, Object> injectedData) throws SoyException {

        }
    }
}