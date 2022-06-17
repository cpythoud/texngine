package org.texngine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TeXngineImpl implements TeXngine {

    private final ExecutorService executor;

    private boolean debug = false;

    protected TeXngineImpl(int threadCount) {
        if (threadCount < 1)
            throw new IllegalArgumentException("There must be at least one thread available. Illegal value: " + threadCount);

        executor = new ThreadPoolExecutor(1, threadCount, 1, TimeUnit.SECONDS, new PriorityBlockingQueue<>());
    }

    protected TeXngineImpl(ExecutorService executor) {
        this.executor = executor;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void execute(TeXCommand texCommand) {
        if (debug)
            texCommand.run();
        else
            executor.execute(texCommand);
    }

    public void shutdown() {
        executor.shutdown();
    }

}
