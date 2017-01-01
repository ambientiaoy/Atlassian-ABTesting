package fi.ambientia.abtesting.action;

import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleEntity;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChooseAWinnerOfAFeatureBattle {

    private final FeatureBattleResults featureBattleResults;
    private final FeatureBattleRepository featureBattleRepository;

    @Autowired
    public ChooseAWinnerOfAFeatureBattle(FeatureBattleResults featureBattleResults, FeatureBattleRepository featureBattleRepository) {
        this.featureBattleResults = featureBattleResults;
        this.featureBattleRepository = featureBattleRepository;
    }

    public void forFeatureBattle(UserIdentifier userIdentifier, FeatureBattleIdentifier featureBattleIdentifier, Experiment.Type type) {
        Optional<FeatureBattleEntity> featureBattleEntity = featureBattleRepository.ensureExistsOnlyOne(featureBattleIdentifier);
        featureBattleEntity.ifPresent( (entity) -> featureBattleResults.newWinnerFor(entity).forUser(userIdentifier).resultBeing(type));
    }
}
