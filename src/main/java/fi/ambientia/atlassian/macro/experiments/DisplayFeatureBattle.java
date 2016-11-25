package fi.ambientia.atlassian.macro.experiments;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import fi.ambientia.abtesting.action.experiments.feature_battles.DisplayFeatureBattleExperiment;
import fi.ambientia.atlassian.users.MapCurrentUserToUserkey;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Map;

public class DisplayFeatureBattle implements Macro {

    private final MapCurrentUserToUserkey mapCurrentUserToUserkey;
    private final DisplayFeatureBattleExperiment displayFeatureBattleExperiment;

    @Autowired
    public DisplayFeatureBattle(MapCurrentUserToUserkey mapCurrentUserToUserkey, DisplayFeatureBattleExperiment displayFeatureBattleExperiment) {
        this.mapCurrentUserToUserkey = mapCurrentUserToUserkey;
        this.displayFeatureBattleExperiment = displayFeatureBattleExperiment;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Serializable currentUserIdentifier = mapCurrentUserToUserkey.getCurrentUserIdentifier();

        return displayFeatureBattleExperiment.displayContent( currentUserIdentifier );
    }

    public BodyType getBodyType() {
        return null;
    }

    public OutputType getOutputType() {
        return null;
    }
}
