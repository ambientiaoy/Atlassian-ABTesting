package ut.fi.ambientia.e2e.bootstrap;

import fi.ambientia.abtesting.model.EventLogger;

public class DummyEventLogger implements EventLogger {
    @Override
    public LoggerOn failed() {
        return ( event ) -> {};
    }

    @Override
    public LoggerOn succes() {
        return ( event ) -> {};
    }
}
