package ut.fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseFeature;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.atlassian.macro.experiments.DisplayFeatureBattle;
import fi.ambientia.atlassian.users.CurrentUser;
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
    private UserManager currentUser;
    private SoyTemplateRenderer renderer;
    private ChooseFeature chooseFeature;
    private DisplayFeatureBattle displayFeatureBattle;
    private Experiment experiment;

    @Before
    public void setUp() throws Exception {
        currentUser = mock(UserManager.class);
        chooseFeature = mock(ChooseFeature.class);
        renderer = new SoyTemplateRendererStub();
        experiment = new GoodOldWay();

        displayFeatureBattle = new DisplayFeatureBattle(renderer, currentUser, chooseFeature);
    }

    @Test
    public void should_render_a_feature_battle_specific_for_a_user() throws Exception {
        when(currentUser.getRemoteUserKey()).thenReturn(new UserKey(USER_KEY));
        when(chooseFeature.forUser( USER_KEY )).thenReturn( experiment );

        String execute = displayFeatureBattle.execute(map, string, conversionContext);

        assertThat(execute, equalTo("RENDERING fi.ambientia.abtesting.model.experiments.GoodOldWay"));
    }

    @Test
    public void render_default_feature_battle_for_anonymous_user() throws Exception {
        when(currentUser.getRemoteUserKey()).thenReturn(null);
        when(chooseFeature.forUser( DisplayFeatureBattle.ANONYMOUS_USER )).thenReturn( experiment );

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