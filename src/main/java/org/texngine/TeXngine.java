package org.texngine;

import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;

public interface TeXngine {

    String NON_STOP_MODE_OPTION = "-interaction=nonstopmode";
    String BATCH_MODE_OPTION    = "-interaction=batchmode";

    static TeXngine create(int threadCount) {
        return new DefaultTeXngine(threadCount);
    }

    static TeXngine create(ExecutorService executor) {
        return new DefaultTeXngine(executor);
    }

    static TeXngine mock() {
        return new MockTeXngine();
    }

    static TeXngine mock(Logger logger) {
        return new MockTeXngine(logger);
    }

    void setDebug(boolean debug);

    void execute(TeXCommand texCommand);

    void shutdown();

    default TeXCommandFactory getCommandFactory() {
        return new DefaultTeXCommandFactory(this);
    }

}
