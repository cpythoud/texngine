package org.texngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultTeXngine implements TeXngine {

    protected final Logger logger = LoggerFactory.getLogger(DefaultTeXngine.class);

    private final ExecutorService executor;

    private boolean debug = false;

    protected DefaultTeXngine(int threadCount) {
        if (threadCount < 1)
            throw new IllegalArgumentException("There must be at least one thread available. Illegal value: " + threadCount);

        executor = new ThreadPoolExecutor(1, threadCount, 1, TimeUnit.SECONDS, new PriorityBlockingQueue<>());
    }

    protected DefaultTeXngine(ExecutorService executor) {
        this.executor = executor;
    }

    public void setDebug(boolean debug) {
        logger.trace("setDebug({}}) called", debug);
        this.debug = debug;
    }

    public void execute(TeXCommand texCommand) {
        if (debug) {
            logger.info("Executing directly (debug mode) command: {}", texCommand);
            texCommand.run();
        } else {
            logger.info("Forking command: {}", texCommand);
            executor.execute(texCommand);
        }
    }

    public void shutdown() {
        logger.info("TeXngine shutdown.");
        executor.shutdown();
    }

}
