package ut.fi.ambientia.e2e.bootstrap;

import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import fi.ambientia.abtesting.action.ChooseAWinnerOfAFeatureBattle;
import fi.ambientia.abtesting.action.UpdateFeatureBattle;
import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.AlreadyDecidedBattles;
import fi.ambientia.abtesting.action.experiments.feature_battles.ChooseExperiment;
import fi.ambientia.abtesting.action.experiments.feature_battles.CreateNewFeatureBattle;
import fi.ambientia.abtesting.action.experiments.feature_battles.ExecuteFeatureBattle;
import fi.ambientia.abtesting.action.experiments.feature_battles.RandomizeFeatureBattle;
import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.ExperimentAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleResultsAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.UserExperimentAORepository;
import fi.ambientia.abtesting.model.EventLogger;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import fi.ambientia.abtesting.model.feature_battles.UserExperimentRepository;
import fi.ambientia.atlassian.macro.experiments.DisplayFeatureBattle;
import fi.ambientia.atlassian.routes.feature_battles.FeatureBattleRoute;
import fi.ambientia.atlassian.routes.feature_battles.FeatureBattleThresholdRoute;
import fi.ambientia.atlassian.routes.feature_battles.FeatureBattleWins;
import fi.ambientia.atlassian.routes.feature_battles.FeatureBattles;
import ut.fi.ambientia.helpers.TestPluginProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Bootstrap {

    private TestPluginProperties properties;
    private ChooseExperiment chooseExperiment;
    private FeatureBattleRoute featureBattleRoute;
    private FeatureBattles featureBattles;
    private DisplayFeatureBattle displayFeatureBattle;

    public HttpServletRequest httpServletRequestMock;
    private Supplier<HttpServletRequest> supplier = () -> httpServletRequestMock;
    private FeatureBattleWins featureBattleWins;
    private final UserManager userManager = mock(UserManager.class);
    private UserExperimentRepository userExperimentRepository;
    private FeatureBattleThresholdRoute featureBattleThresholdRoute;

    public void bootstrap(SimpleActiveObjects sao) {

        httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getParameter("featureBattleWinner")).thenReturn(null);

        properties = new TestPluginProperties();
        properties.setProperty("default.abtest.space.key", "FOOBAR");

        // Repositories
        ExperimentAORepository experimentRepository = new ExperimentAORepository(sao, properties);
        FeatureBattleAORepository featureBattleRepository = new FeatureBattleAORepository(sao, properties, experimentRepository);
        FeatureBattleResults featureBattleResults= new FeatureBattleResultsAORepository(sao, properties);
        userExperimentRepository = new UserExperimentAORepository(sao, properties, featureBattleRepository);

        // Services
        RandomizeFeatureBattle randomizeFeatureBattle = new RandomizeFeatureBattle( featureBattleRepository, experimentRepository );
        ExecuteFeatureBattle executeFeatureBattle = new ExecuteFeatureBattle(featureBattleRepository, experimentRepository);
        AlreadyDecidedBattles alreadyDecidedBattles = new AlreadyDecidedBattles(featureBattleResults, userExperimentRepository);
        EventLogger eventLogger = new DummyEventLogger();

        // Actions
        chooseExperiment = new ChooseExperiment( alreadyDecidedBattles, executeFeatureBattle, randomizeFeatureBattle);
        CreateExperiment createExperiment = new CreateNewFeatureBattle( featureBattleRepository, experimentRepository);
        ChooseAWinnerOfAFeatureBattle chooseAWinnerOfAFeatureBattle = new ChooseAWinnerOfAFeatureBattle( featureBattleResults, featureBattleRepository, eventLogger);
        UpdateFeatureBattle updateFeatureBattle = new UpdateFeatureBattle();

        // Routes
        featureBattleRoute = new FeatureBattleRoute(createExperiment, featureBattleRepository);
        featureBattles = new FeatureBattles(createExperiment, featureBattleRoute, featureBattleRepository);
        featureBattleWins = new FeatureBattleWins(featureBattleRoute, chooseAWinnerOfAFeatureBattle);
        featureBattleThresholdRoute = new FeatureBattleThresholdRoute( updateFeatureBattle );

        // Confluence Mocks
        when(userManager.getRemoteUserKey()).thenReturn( new UserKey("ANY USER"));

        // Macros
        displayFeatureBattle = new DisplayFeatureBattle(userManager, chooseExperiment, properties){
            @Override
            protected Supplier<Map<String, Object>> getVelocityContextSupplier() {
                return () -> {
                    HashMap<String, Object> objectObjectHashMap = new HashMap<>();
                    objectObjectHashMap.put("req", supplier.get());
                    return objectObjectHashMap;
                };
            }

            @Override
            protected Supplier<String> getRenderedTemplate(Map<String, Object> contextMap) {
                return () -> {
                    return contextMap.get("experiment") == null ?
                            "null" :
                            ((Experiment) contextMap.get("experiment")).render();
                };
            }
        };
    }

    public TestPluginProperties getProperties() {
        return properties;
    }

    public ChooseExperiment getChooseExperiment() {
        return chooseExperiment;
    }

    public FeatureBattles getFeatureBattles() {
        return featureBattles;
    }

    public DisplayFeatureBattle getDisplayBattle() {
        return displayFeatureBattle;
    }

    public FeatureBattleRoute getFeatureBattleRoute() {
        return featureBattleRoute;
    }

    public FeatureBattleWins getFeatureBattleWins() {
        return featureBattleWins;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public FeatureBattleThresholdRoute getFeatureBattleThresholdRoute() {
        return featureBattleThresholdRoute;
    }
}
