package fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import fi.ambientia.abtesting.action.experiments.feature_battles.DisplayFeatureBattleExperiment;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.atlassian.PluginConstants;
import fi.ambientia.atlassian.users.MapCurrentUserToUserkey;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DisplayFeatureBattle implements Macro {

    private final SoyTemplateRenderer renderer;
    private final MapCurrentUserToUserkey mapCurrentUserToUserkey;
    private final DisplayFeatureBattleExperiment displayFeatureBattleExperiment;

    @Autowired
    public DisplayFeatureBattle(@ComponentImport SoyTemplateRenderer renderer,  MapCurrentUserToUserkey mapCurrentUserToUserkey, DisplayFeatureBattleExperiment displayFeatureBattleExperiment) {
        this.renderer = renderer;
        this.mapCurrentUserToUserkey = mapCurrentUserToUserkey;
        this.displayFeatureBattleExperiment = displayFeatureBattleExperiment;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Serializable currentUserIdentifier = mapCurrentUserToUserkey.getCurrentUserIdentifier();

        Experiment macroDef = displayFeatureBattleExperiment.displayContent(currentUserIdentifier);

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
