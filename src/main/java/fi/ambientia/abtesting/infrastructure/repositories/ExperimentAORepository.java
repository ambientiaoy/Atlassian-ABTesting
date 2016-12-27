package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class ExperimentAORepository implements ExperimentRepository {
    private final ActiveObjects ao;
    private final PluginProperties properties;
    private final Random random;

    public ExperimentAORepository(ActiveObjects ao, PluginProperties properties) {
        this.ao = ao;
        this.properties = properties;
        this.random = new Random();
    }

    @Override
    public List<Experiment> experimentsForUser(UserIdentifier userientifier) {
        Query query = Query.select().from(UserExperimentAO.class).where("USER_ID = ?", userientifier.getIdentifier());
        UserExperimentAO[] userExperimentAOs = ao.find(UserExperimentAO.class, query);

        List<Experiment> experiments = Arrays.asList(userExperimentAOs).stream().
                map(userExperimentAO -> Experiment.forType(userExperimentAO.getExperimentType()).withIdentifier(userExperimentAO.getExperimentId())).
                collect(Collectors.toList());

        return experiments;
    }


}
