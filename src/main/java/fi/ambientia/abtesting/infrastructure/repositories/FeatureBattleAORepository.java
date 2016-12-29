package fi.ambientia.abtesting.infrastructure.repositories;

import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.IdResolver;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.PageObject;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattle;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.atlassian.PluginConstants;
import fi.ambientia.atlassian.properties.PluginProperties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Repository
public class FeatureBattleAORepository implements FeatureBattleRepository{

    Logger logger = Logger.getLogger(FeatureBattleAORepository.class);

    public static final int DEFAULT_THRESHOLD = 10;
    // AO is imported via META-INF/spring/plugin-context.xml
    private final SimpleActiveObjects ao;
    private final PluginProperties properties;
    private final ExperimentAORepository experimentAORepository;
    private Random random;

    @Autowired
    public FeatureBattleAORepository(@Qualifier("TransactionalActiveObject") SimpleActiveObjects ao, PluginProperties properties, ExperimentAORepository experimentAORepository) {
        this.ao = ao;
        this.properties = properties;
        this.experimentAORepository = experimentAORepository;
        random = new Random();
    }

    @Override
    public IdResolver createFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier) {
        logger.info("Creating a new featureBattle " + featureBattleIdentifier.getIdentifier());
        Integer id = ao.withinTransaction(() -> {

            Optional<FeatureBattleAO> featureBattleAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "FEATURE_BATTLE_ID = ?", featureBattleIdentifier.getIdentifier());
            if(featureBattleAO.isPresent()){
                return featureBattleAO.get().getID();
            }
            FeatureBattleAO experimentAO = ao.create(FeatureBattleAO.class);
            experimentAO.setFeatureBattleId( featureBattleIdentifier.getIdentifier() );
            experimentAO.setThreshold( properties.propertyOrDefault("experiment.default.threshold", DEFAULT_THRESHOLD));
            experimentAO.save();
            logger.debug("Created a new featureBattle with id: " + experimentAO.getID());
            return experimentAO.getID();
        });
        return () -> id;
    }

    @Override
    public void setThreshold(Integer featureBattleIdentifier, int threshold) {
        ao.withinTransaction( () -> {

            Optional<FeatureBattleAO> optional = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "ID = ? ", featureBattleIdentifier);
            optional.ifPresent(( featureBattleAO ) -> {
                featureBattleAO.setThreshold( threshold );
                featureBattleAO.save();
            });
            return null;
        });
    }

    @Override
    public CreateNewFeatureBattleFor newFeatureBattleFor(FeatureBattleIdentifier featureBattleIdentifier) {
        logger.info("Creating new featureBattle for User ");
        Optional<FeatureBattleAO> fbAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", featureBattleIdentifier.getIdentifier());
        List<ExperimentAO> experimentAOs = Arrays.asList(fbAO.get().getExperiments());

        return ( user) -> {
            return ( experiment ) -> {
                logger.info("Creating new featureBattle '"+ featureBattleIdentifier.getIdentifier() + "'for User " + user.getIdentifier() + " result being " + experiment.type().toString() );
                Optional<ExperimentAO> experimentAOOptional = experimentAOs.stream().
                        filter((experimentAO -> experimentAO.getExperimentType().equals(experiment.type()))).
                        findFirst();

                ao.withinTransaction( () ->{

                    UserExperimentAO userExperimentAO = getUserExperimentAO(user.getIdentifier(), featureBattleIdentifier.getIdentifier());
                    // FIXME AkS: I See a null here!
                    userExperimentAO.setExperiment( experimentAOOptional.orElse( null ) );
                    userExperimentAO.setUserId( user.getIdentifier() );
                    userExperimentAO.setFeatureBattleId( featureBattleIdentifier.getIdentifier() );
                    userExperimentAO.setExperimentType( experiment.type() );
                    userExperimentAO.save();
                    logger.debug("Created '" +userExperimentAO.getID() );
                    return null;
                });

            };
        };
    }

    protected UserExperimentAO getUserExperimentAO(String userId, String featureBattleId) {
        Optional<UserExperimentAO> userExperimentAO1 = EnsureOnlyOneAOEntityExists.execute(ao, UserExperimentAO.class, "USER_ID = ? AND FEATURE_BATTLE_id = ?", userId, featureBattleId );

        return userExperimentAO1.orElseGet(() -> ao.create(UserExperimentAO.class));
    }

    @Override
    public Experiment randomBattleResultFor(FeatureBattleIdentifier identifier) {
        Optional<FeatureBattleAO> featureBattleAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", identifier.getIdentifier());

        Optional<Experiment.Type> type = featureBattleAO.map(featureBattleAO1 -> Experiment.randomize(random, featureBattleAO1.getThreshold(), identifier));

        Optional<ExperimentAO> experimentAO = featureBattleAO.flatMap(
                (fbAO) -> type.flatMap(
                        (_type) -> EnsureOnlyOneAOEntityExists.execute(ao, ExperimentAO.class, "FEATURE_BATTLE_ID = ? AND EXPERIMENT_TYPE = ?", fbAO.getID(), _type.name()))
        );

        return experimentAO.map(
                (_ao) -> Experiment.forType( _ao.getExperimentType() ).withIdentifier( _ao.getExperimentId(),
                        new PageObject(properties.propertyOrDefault("default.abtest.space.key", PluginConstants.DEFAULT_SPACE_KEY), _ao.getPage() ) )
        ).orElse(Experiment.missingExperiment() );
    }

    @Override
    public List<FeatureBattle> getAll() {
        List<FeatureBattleAO> featureBattleAOs = Arrays.asList(ao.find(FeatureBattleAO.class));
        logger.debug("All featurebattles: \n\t" + featureBattleAOs.stream().map( featureBattleAO -> describeFeatureBattle(featureBattleAO)).collect( Collectors.joining("\n\t")));
        return featureBattleAOs.stream().map( item -> {
            List<ExperimentAO> experimentAOs = Arrays.asList(item.getExperiments());
            return new FeatureBattle( new FeatureBattleIdentifier( item.getFeatureBattleId() ), experimentAOs.stream().
                    map ( experimentAO -> ExperimentAORepository.buildExperiment( properties, experimentAO)).collect(Collectors.toList()));
        }).collect(Collectors.toList());
    }

    protected String describeFeatureBattle(FeatureBattleAO featureBattleAO) {
        return  String.format("FeatureBattle: %s, with Identifier %s, and threshold %d, and experiments %s ",
                featureBattleAO.getID(),
                featureBattleAO.getFeatureBattleId(),
                featureBattleAO.getThreshold(),
                Arrays.asList(featureBattleAO.getExperiments()).stream().
                        map(experimentAO -> "Exp " + experimentAO.getExperimentType() + " on page " + experimentAO.getPage() ).
                        collect(Collectors.joining("\n\t\t"))
        );
    }


    @Override
    public Optional<FeatureBattle> getFeatureBattle(FeatureBattleIdentifier featureBattleIdentifier) {
        logger.debug("GetFeatureBattle for " + featureBattleIdentifier );
        Optional<FeatureBattleAO> featureBattleAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "FEATURE_BATTLE_ID = ? ", featureBattleIdentifier.getIdentifier());
        return featureBattleAO.map( entity -> {
            List<ExperimentAO> experimentAOs = Arrays.asList(entity.getExperiments());
            return new FeatureBattle(new FeatureBattleIdentifier( entity.getFeatureBattleId() ), experimentAOs.stream().
                    map(experimentAO -> ExperimentAORepository.buildExperiment(properties, experimentAO)).collect(Collectors.toList()));
        });
    }
}
