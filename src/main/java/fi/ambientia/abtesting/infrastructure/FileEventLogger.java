package fi.ambientia.abtesting.infrastructure;

import fi.ambientia.abtesting.model.EventLogger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileEventLogger implements EventLogger {

    private static Logger logger = Logger.getLogger(FileEventLogger.class);

    @Override
    public LoggerOn failed() {
        return ( event ->  logger.error("Could not create event: " + event.toString()) ) ;
    }

    @Override
    public LoggerOn succes() {
        return ( event ->  logger.info("Created new event: " + event.toString()) ) ;
    }
}
