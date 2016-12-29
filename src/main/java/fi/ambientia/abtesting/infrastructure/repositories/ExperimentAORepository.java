package fi.ambientia.abtesting.infrastructure.repositories;

import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRepository;
import fi.ambientia.abtesting.model.experiments.PageObject;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.PluginConstants;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.Query;
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
public class ExperimentAORepository implements ExperimentRepository {
    private static Logger logger = Logger.getLogger(ExperimentAORepository.class);

    private final SimpleActiveObjects ao;
    private final PluginProperties properties;

    public static Experiment buildExperiment(PluginProperties properties, ExperimentAO experiment) {
        if( null != experiment ){
            logger.debug("building experiment: " + experiment.getExperimentType() + ": " + experiment.getPage());
        } else {
            logger.error("experiment is null!!!!1!!!11!!");
        }
        return Experiment.forType(
                experiment.getExperimentType()).withIdentifier(experiment.getExperimentId(),
                new PageObject(
                        properties.propertyOrDefault("default.abtest.space.key", PluginConstants.DEFAULT_SPACE_KEY),
                        experiment.getPage() ) );
    }

    @Autowired
    public ExperimentAORepository(@Qualifier("TransactionalActiveObject") SimpleActiveObjects ao, PluginProperties properties) {
        this.ao = ao;
        this.properties = properties;
    }

    @Override
    public List<Experiment> experimentsForUser(UserIdentifier userientifier) {
        logger.debug("experimentsForUser" + userientifier.getIdentifier() );
//        return ao.withinTransaction( () -> {
            Query query = Query.select().from(UserExperimentAO.class).where("USER_ID = ?", userientifier.getIdentifier());
            UserExperimentAO[] userExperimentAOs = ao.find(UserExperimentAO.class, query);

            List<Experiment> experiments = Arrays.asList(userExperimentAOs).stream().
                    // TODO AkS: This should basically be fixed by something else. Now the DB is wrong, somewhere
                    filter( userExperimentAO1 -> null != userExperimentAO1.getExperiment() ).
                    map(userExperimentAO -> buildExperiment(properties, userExperimentAO.getExperiment())).
                    collect(Collectors.toList());

            logger.debug("Experiments for user: " + experiments.stream().map(experiment -> "Exp: " + experiment.type() + " for page: " + experiment.page()).collect(Collectors.joining(", ")));

            return experiments;

//        });
    }

    @Override
    public CreateNewExperiment create(Integer featureBattleId, Experiment.Type experimentType) {
        // TODO AkS: what to do if featureBattle is not found!
        Optional<FeatureBattleAO> featureBattleAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "ID = ?", featureBattleId);

        return ( page ) -> {
            Optional<ExperimentAO> optional = EnsureOnlyOneAOEntityExists.execute(ao, ExperimentAO.class, "EXPERIMENT_TYPE = ? and PAGE = ? and FEATURE_BATTLE_ID = ?", experimentType, page, featureBattleId);
            Integer id = ao.withinTransaction( () -> {
                        ExperimentAO experimentAO = optional.orElse(ao.create(ExperimentAO.class));

                        //FIXME AkS: I see null here!
                        experimentAO.setFeatureBattle( featureBattleAO.orElse( null ) );
                        experimentAO.setExperimentType( experimentType );
                        //FIXME AkS: I see null here!
                        experimentAO.setExperimentId( featureBattleAO.map( (fb) -> fb.getFeatureBattleId() ).orElse( null )  );
                        experimentAO.setPage( page );
                        experimentAO.save();
                        logger.debug("Created a featurebattle for : " + featureBattleId + " experiment of type " + experimentType + " and page " + page);
                        return experimentAO.getID();
                    }
            );

            return () -> id;
        };
    }


}
