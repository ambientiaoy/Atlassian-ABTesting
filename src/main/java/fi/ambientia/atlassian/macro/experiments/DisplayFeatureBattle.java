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
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.PluginConstants;
import fi.ambientia.atlassian.properties.PluginProperties;
import fi.ambientia.atlassian.routes.Routes;
import fi.ambientia.atlassian.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.IllegalFormatCodePointException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component("DisplayFeatureBattle")
public class DisplayFeatureBattle implements Macro {

    public static final String TEMPLATES_FEATUREBATTLE_VM = "/templates/featurebattle.vm";

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
        String abtestSpaceKey = properties.propertyOrDefault("default.abtest.space.key", PluginConstants.DEFAULT_SPACE_KEY);

        // get winner from Action parameters, if present!
        Optional<String> httpRequestParameters = getHttpRequestParameters();
        Optional<Experiment.Type> winningExperiment = getType( httpRequestParameters.orElse("NONE") );
        // ((ViewPageAction) contextMap.get("action")).getCurrentRequest().getParameterMap()

        // execute action
        Predicate<FeatureBattleResult> predicate = winningExperiment.
                map(ChooseExperiment::forExperimentType).
                orElse(ChooseExperiment.forUser(new UserIdentifier( currentUserIdentifier )));

        Experiment experiment = chooseFeature.forFeatureBattle(new UserIdentifier(currentUserIdentifier), new FeatureBattleIdentifier(feature_battle_identifier)).matching(predicate);
//        Experiment experiment =  chooseFeature.forFeatureBattle( new UserIdentifier( currentUserIdentifier ), new FeatureBattleIdentifier(feature_battle_identifier));

        // create context and render
        Map<String, Object> contextMap = getVelocityContextSupplier().get();

        contextMap.put("userIdentifier", currentUserIdentifier);
        contextMap.put("featureBattle", feature_battle_identifier);
        contextMap.put("experimentType", experiment.type());
        contextMap.put("experiment", experiment);
        return getRenderedTemplate(contextMap).get();
    }

    protected Optional<Experiment.Type> getType(String winner) {
        try{
            String winnerInCaps = winner.toUpperCase();
            return Optional.of(Experiment.Type.valueOf(winnerInCaps));
        } catch (IllegalArgumentException ex){
            // TODO AkS: add logger
            return Optional.empty();
        }
    }

    protected Optional<String> getHttpRequestParameters() {

        Optional<HttpServletRequest> req = Optional.ofNullable( (HttpServletRequest) getVelocityContextSupplier().get().get("req"));
        return  req.map( request -> request.getParameter("featureBattleWinner"));
    }

    protected Supplier<String> getRenderedTemplate(Map<String, Object> contextMap) {
        return () -> VelocityUtils.getRenderedTemplate(TEMPLATES_FEATUREBATTLE_VM, contextMap);
    }

    protected  Supplier<Map<String, Object >> getVelocityContextSupplier() {
        return () -> MacroUtils.defaultVelocityContext();
    }

    public BodyType getBodyType() {
        return BodyType.RICH_TEXT;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}
