package ut.fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseFeature;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.macro.experiments.DisplayFeatureBattle;
import fi.ambientia.atlassian.users.Users;
import org.junit.Before;
import org.junit.Test;
import ut.fi.ambientia.matchers.UserIdentifierMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ut.fi.ambientia.matchers.ExperimentIdentifierMatcher.matchesWith;

public class DisplayFeatureBattleShould {


    public static final String USER_KEY = "USER KEY";
    public static final UserIdentifier USER_IDENTIFIER = new UserIdentifier(USER_KEY);
    public static final String EXPERIMENT_ID = "EXPERIMENT_ID";
    private Map<String, String> map = new HashMap<>();
    private String string;
    private ConversionContext conversionContext;
    private UserManager userManager;
    private ChooseFeature chooseFeature;
    private DisplayFeatureBattle displayFeatureBattle;
    private Experiment experiment;

    @Before
    public void setUp() throws Exception {
        userManager = mock(UserManager.class);
        chooseFeature = mock(ChooseFeature.class);
        experiment = new GoodOldWay();
        map.put("feature_battle", EXPERIMENT_ID);

        displayFeatureBattle = new DisplayFeatureBattle(userManager, chooseFeature){
            @Override
            protected Supplier<Map<String, Object>> getVelocityContextSupplier() {
                return () -> new HashMap<>();
            }

            @Override
            protected Supplier<String> getRenderedTemplate(Map<String, Object> contextMap) {
                return () -> {
                    return contextMap.get("experiment") == null ?
                            "null" :
                            contextMap.get("experiment").getClass().getName();
                };
            }
        };
    }

    @Test
    public void should_render_a_feature_battle_specific_for_a_user() throws Exception {
        when(userManager.getRemoteUserKey()).thenReturn(new UserKey(USER_KEY));
        when(chooseFeature.forUser( argThat( UserIdentifierMatcher.matchesWithUser(USER_IDENTIFIER)), any( ExperimentIdentifier.class ) ) ).thenReturn( experiment );

        String execute = displayFeatureBattle.execute(map, string, conversionContext);

        assertThat(execute, equalTo("fi.ambientia.abtesting.model.experiments.GoodOldWay"));
    }

    @Test
    public void render_default_feature_battle_for_anonymous_user() throws Exception {
        when(userManager.getRemoteUserKey()).thenReturn(null);
        when(chooseFeature.forUser(argThat( UserIdentifierMatcher.matchesWithUser( new UserIdentifier( Users.ANONYMOUS_USER) )), any( ExperimentIdentifier.class ) )).thenReturn( experiment );

        String execute = displayFeatureBattle.execute(map, string, conversionContext);

        assertThat(execute, equalTo("fi.ambientia.abtesting.model.experiments.GoodOldWay"));
    }

    @Test
    public void find_a_feature_battle_for_specific_identifier() throws Exception {
        when(chooseFeature.forUser(any(UserIdentifier.class), argThat( matchesWith( new ExperimentIdentifier(EXPERIMENT_ID) )))).thenReturn( experiment );

        String execute = displayFeatureBattle.execute(map, string, conversionContext);

        assertThat(execute, equalTo("fi.ambientia.abtesting.model.experiments.GoodOldWay"));
    }

}