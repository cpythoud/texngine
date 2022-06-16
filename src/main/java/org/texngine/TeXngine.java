package org.texngine;

import java.util.concurrent.ExecutorService;

public interface TeXngine {

    static TeXngine create(int threadCount) {
        return new TeXngineImpl(threadCount);
    }

    static TeXngine create(ExecutorService executor) {
        return new TeXngineImpl(executor);
    }

    void setDebug(boolean debug);

    void execute(TeXCommandImpl texCommand);

    void shutdown();

    default TeXCommandFactory getCommandFactory() {
        return new TeXCommandFactoryImpl();
    }

}
