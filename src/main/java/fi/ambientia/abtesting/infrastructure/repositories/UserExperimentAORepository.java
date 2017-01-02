package fi.ambientia.abtesting.infrastructure.repositories;

import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.feature_battles.UserExperimentRepository;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserExperimentAORepository implements UserExperimentRepository {
    private final SimpleActiveObjects sao;
    private final PluginProperties properties;
    private final FeatureBattleRepository featureBattleRepository;

    @Autowired
    public UserExperimentAORepository(@Qualifier("TransactionalActiveObject") SimpleActiveObjects sao, PluginProperties properties, FeatureBattleRepository featureBattleRepository) {
        this.sao = sao;
        this.properties = properties;
        this.featureBattleRepository = featureBattleRepository;
    }

    @Override
    public List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier featureBattleIdentifier, UserIdentifier user) {
        UserExperimentAO[] userExperimentAOs = sao.find(UserExperimentAO.class, Query.select().where("FEATURE_BATTLE_ID = ? AND USER_ID = ?", featureBattleIdentifier.getFeatureBattleId(), user.getIdentifier()));
        List<FeatureBattleResult> collect = Arrays.asList(userExperimentAOs).stream().
                map(userExperimentAO ->
                        new FeatureBattleResult(new UserIdentifier(userExperimentAO.getUserId()), ExperimentAORepository.buildExperiment(properties, userExperimentAO.getExperiment()))).
                collect(Collectors.toList());
        return collect;

    }
}
