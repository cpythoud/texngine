package org.texngine;

import org.beanmaker.v2.util.logging.Logger;
import org.beanmaker.v2.util.logging.SoutLogger;

public class MockTeXngine implements TeXngine {

    private final Logger logger;

    public MockTeXngine() {
        this(new SoutLogger());
    }

    public MockTeXngine(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void setDebug(boolean debug) {
        logger.info("MockTeXngine.setDebug(" + debug + ") called");
    }

    @Override
    public void execute(TeXCommand texCommand) {
        texCommand.run();
    }

    @Override
    public void shutdown() {
        logger.info("MockTeXngine.shutdown() called");
    }

    @Override
    public TeXCommandFactory getCommandFactory() {
        return new MockTeXCommandFactory(this, logger);
    }

}
