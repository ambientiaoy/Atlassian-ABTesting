package fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseFeature;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.atlassian.PluginConstants;
import fi.ambientia.atlassian.users.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DisplayFeatureBattle implements Macro {

    private final SoyTemplateRenderer renderer;
    private final CurrentUser currentUser;
    private final ChooseFeature chooseFeature;

    @Autowired
    public DisplayFeatureBattle(@ComponentImport SoyTemplateRenderer renderer, CurrentUser currentUser, ChooseFeature chooseFeature) {
        this.renderer = renderer;
        this.currentUser = currentUser;
        this.chooseFeature = chooseFeature;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Serializable currentUserIdentifier = currentUser.getIdentifier();

        Experiment macroDef = chooseFeature.forUser(currentUserIdentifier);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("macroDef", macroDef);
        return renderer.render(PluginConstants.SOY_TEMPLATES, "Confluence.Templates.ABTesting.featurebattle.display", data);
    }

    public BodyType getBodyType() {
        return null;
    }

    public OutputType getOutputType() {
        return null;
    }
}
