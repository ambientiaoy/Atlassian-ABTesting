package fi.ambientia.abtesting.action;

import fi.ambientia.abtesting.events.ChooseAWinnerEvent;
import fi.ambientia.abtesting.model.EventLogger;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleEntity;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChooseAWinnerOfAFeatureBattle {

    private final FeatureBattleResults featureBattleResults;
    private final FeatureBattleRepository featureBattleRepository;
    private final EventLogger eventLogger;

    @Autowired
    public ChooseAWinnerOfAFeatureBattle(FeatureBattleResults featureBattleResults, FeatureBattleRepository featureBattleRepository, EventLogger eventLogger) {
        this.featureBattleResults = featureBattleResults;
        this.featureBattleRepository = featureBattleRepository;
        this.eventLogger = eventLogger;
    }

    public void forFeatureBattle(ChooseAWinnerEvent chooseAWinnerEvent) {
        Optional<FeatureBattleEntity> featureBattleEntity = featureBattleRepository.ensureExistsOnlyOne(chooseAWinnerEvent.getFeatureBattleIdentifier());


        featureBattleEntity.ifPresent( (entity) -> featureBattleResults.newWinnerFor(entity).forUser(chooseAWinnerEvent.getUserIdentifier()).resultBeing(chooseAWinnerEvent.getType()));
        EventLogger.staticLogSuccesIfPresent( eventLogger, featureBattleEntity ).on( chooseAWinnerEvent );

    }
}
