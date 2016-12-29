package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
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
    private final SimpleActiveObjects ao;
    private final PluginProperties properties;
    private final Random random;

    public static Experiment buildExperiment(PluginProperties properties, ExperimentAO experiment) {
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
        this.random = new Random();
    }

    @Override
    public List<Experiment> experimentsForUser(UserIdentifier userientifier) {
        Query query = Query.select().from(UserExperimentAO.class).where("USER_ID = ?", userientifier.getIdentifier());
        UserExperimentAO[] userExperimentAOs = ao.find(UserExperimentAO.class, query);

        List<Experiment> experiments = Arrays.asList(userExperimentAOs).stream().
                map(userExperimentAO -> buildExperiment(properties, userExperimentAO.getExperiment())).
                collect(Collectors.toList());

        return experiments;
    }

    @Override
    public CreateNewExperiment create(Integer featureBattleId, Experiment.Type experimentType) {
        // TODO AkS: what to do if featureBattle is not found!
        Optional<FeatureBattleAO> featureBattleAO = EnsureOnlyOneAOEntityExists.execute(ao, FeatureBattleAO.class, "ID = ?", featureBattleId);

        return ( page ) -> {
            Optional<ExperimentAO> optional = EnsureOnlyOneAOEntityExists.execute(ao, ExperimentAO.class, "EXPERIMENT_TYPE = ? and PAGE = ?", experimentType, page);
            ExperimentAO experimentAO = optional.orElse(ao.create(ExperimentAO.class));
            //FIXME AkS: I see null here!
            experimentAO.setFeatureBattle( featureBattleAO.orElse( null ) );
            experimentAO.setExperimentType( experimentType );
            //FIXME AkS: I see null here!
            experimentAO.setExperimentId( featureBattleAO.map( (fb) -> fb.getFeatureBattleId() ).orElse( null )  );
            experimentAO.setPage( page );
            experimentAO.save();
            return () -> experimentAO.getID();
        };
    }


}
