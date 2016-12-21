package fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseFeature;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.atlassian.PluginConstants;
import fi.ambientia.atlassian.users.Users;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DisplayFeatureBattle implements Macro {

    private final SoyTemplateRenderer renderer;
    private final Supplier<String> currentUser;
    private final ChooseFeature chooseFeature;

    @Autowired
    public DisplayFeatureBattle(@ComponentImport SoyTemplateRenderer renderer, @ComponentImport final UserManager userManager, ChooseFeature chooseFeature) {
        this.renderer = renderer;
        this.currentUser = Users.getCurrentUserKey(userManager);
        this.chooseFeature = chooseFeature;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        String currentUserIdentifier = currentUser.get();

        Experiment macroDef = chooseFeature.forUser(currentUserIdentifier);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("macroDef", macroDef);
        return renderer.render(PluginConstants.SOY_TEMPLATES , "Confluence.Templates.ABTesting.featurebattle.display", data);
    }

    public BodyType getBodyType() {
        return null;
    }

    public OutputType getOutputType() {
        return null;
    }
}
