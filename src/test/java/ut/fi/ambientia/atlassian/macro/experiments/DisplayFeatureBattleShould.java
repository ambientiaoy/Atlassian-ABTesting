package ut.fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseFeature;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.atlassian.macro.experiments.DisplayFeatureBattleVm;
import fi.ambientia.atlassian.users.Users;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DisplayFeatureBattleShould {


    public static final String USER_KEY = "USER_KEY";
    private Map<String, String> map;
    private String string;
    private ConversionContext conversionContext;
    private UserManager userManager;
    private SoyTemplateRenderer renderer;
    private ChooseFeature chooseFeature;
    private DisplayFeatureBattleVm displayFeatureBattle;
    private Experiment experiment;

    @Before
    public void setUp() throws Exception {
        userManager = mock(UserManager.class);
        chooseFeature = mock(ChooseFeature.class);
        experiment = new GoodOldWay();

        displayFeatureBattle = new DisplayFeatureBattleVm(renderer, userManager, chooseFeature){
            @Override
            protected Supplier<Map<String, Object>> getVelocityContextSupplier() {
                return () -> new HashMap<>();
            }

            @Override
            protected Supplier<String> getRenderedTemplate(Map<String, Object> contextMap) {
                return () -> contextMap.get("experiment").getClass().getName();
            }
        };
    }

    @Test
    public void should_render_a_feature_battle_specific_for_a_user() throws Exception {
        when(userManager.getRemoteUserKey()).thenReturn(new UserKey(USER_KEY));
        when(chooseFeature.forUser( USER_KEY )).thenReturn( experiment );

        String execute = displayFeatureBattle.execute(map, string, conversionContext);

        assertThat(execute, equalTo("fi.ambientia.abtesting.model.experiments.GoodOldWay"));
    }

    @Test
    public void render_default_feature_battle_for_anonymous_user() throws Exception {
        when(userManager.getRemoteUserKey()).thenReturn(null);
        when(chooseFeature.forUser( Users.ANONYMOUS_USER )).thenReturn( experiment );

        String execute = displayFeatureBattle.execute(map, string, conversionContext);

        assertThat(execute, equalTo("fi.ambientia.abtesting.model.experiments.GoodOldWay"));
    }
}