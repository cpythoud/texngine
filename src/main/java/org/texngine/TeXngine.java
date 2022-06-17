package org.texngine;

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

    void setDebug(boolean debug);

    void execute(TeXCommand texCommand);

    void shutdown();

    default TeXCommandFactory getCommandFactory() {
        return new DefaultTeXCommandFactory(this);
    }

}
