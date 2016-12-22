package fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseFeature;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
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
    private final ChooseFeature chooseFeature;


    @Autowired
    public DisplayFeatureBattle(@ComponentImport final UserManager userManager, ChooseFeature chooseFeature) {
        this.currentUser = Users.getCurrentUserKey(userManager);
        this.chooseFeature = chooseFeature;
    }

    public String execute(Map<String, String> parameter, String s, ConversionContext conversionContext) throws MacroExecutionException {
        String currentUserIdentifier = currentUser.get();

        Experiment experiment =  chooseFeature.forUser( new UserIdentifier( currentUserIdentifier ), new ExperimentIdentifier( parameter.get("feature_battle")));

        Map<String, Object> contextMap = getVelocityContextSupplier().get();
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
