package fi.ambientia.abtesting.model;

import fi.ambientia.abtesting.events.Event;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleEntity;

import java.util.Optional;

public interface EventLogger {
    LoggerOn failed();

    LoggerOn succes();

    static LoggerOn staticLogSuccesIfPresent(EventLogger eventLogger, Optional optional) {
        if(optional.isPresent()){
            return eventLogger.succes();
        }else{
            return eventLogger.failed();
        }
    }

    @FunctionalInterface
    interface LoggerOn {
        void on(Event event);
    }

    default LoggerOn logSuccesIfPresent(Optional optional){
        return staticLogSuccesIfPresent(this, optional);
    }
}
