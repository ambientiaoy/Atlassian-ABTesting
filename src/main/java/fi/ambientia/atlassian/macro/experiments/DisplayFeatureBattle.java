package fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseExperiment;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.properties.PluginProperties;
import fi.ambientia.atlassian.routes.Routes;
import fi.ambientia.atlassian.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component("DisplayFeatureBattle")
public class DisplayFeatureBattle implements Macro {

    public static final String TEMPLATES_FEATUREBATTLE_VM = "/templates/featurebattle.vm";
    public static final String SPACE_KEY = "ABTEST";

    private final Supplier<String> currentUser;
    private final ChooseExperiment chooseFeature;
    private final PluginProperties properties;


    @Autowired
    public DisplayFeatureBattle(@ComponentImport final UserManager userManager, ChooseExperiment chooseFeature, PluginProperties properties) {
        this.properties = properties;
        this.currentUser = Users.getCurrentUserKey(userManager);
        this.chooseFeature = chooseFeature;
    }

    public String execute(Map<String, String> parameter, String s, ConversionContext conversionContext) throws MacroExecutionException {
        // handle input parameters
        String currentUserIdentifier = currentUser.get();
        String feature_battle_identifier = Routes.getParameter(parameter, "feature_battle", () -> FeatureBattleIdentifier.DEFAULT_IDENTIFIER);
        String abtestSpaceKey = properties.propertyOrDefault("default.abtest.space.key", "ABTEST");

        // get winner from Action parameters, if present!
        Map<String, Object> contextMap = getVelocityContextSupplier().get();
        // ((ViewPageAction) contextMap.get("action")).getCurrentRequest().getParameterMap()

        // execute action
        Experiment experiment =  chooseFeature.forUser( new UserIdentifier( currentUserIdentifier ), new FeatureBattleIdentifier(feature_battle_identifier));

        // create context and render
        contextMap.put("experiment", experiment);
        return getRenderedTemplate(contextMap).get();
    }

    protected Supplier<String> getRenderedTemplate(Map<String, Object> contextMap) {
        return () -> VelocityUtils.getRenderedTemplate(TEMPLATES_FEATUREBATTLE_VM, contextMap);
    }

    protected  Supplier<Map<String, Object >> getVelocityContextSupplier() {
        return () -> MacroUtils.defaultVelocityContext();
    }

    public String getPageToBeRendered(){
        return "{include:" + SPACE_KEY +":" + "b1_good_old" + "}";
    }

    public BodyType getBodyType() {
        return BodyType.RICH_TEXT;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}
