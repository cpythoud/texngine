package org.texngine;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TeXngine {

    public static final String NON_STOP_MODE_OPTION = "-interaction=nonstopmode";
    public static final String BATCH_MODE_OPTION    = "-interaction=batchmode";

    private final File baseDir;
    private final ExecutorService executor;

    private boolean debug = false;

    public TeXngine(final String baseDirPath, final int threadCount) {
        if (threadCount < 1)
            throw new IllegalArgumentException("There must be at least one thread available. Illegal value: " + threadCount);

        baseDir = new File(baseDirPath);
        executor = new ThreadPoolExecutor(1, threadCount, 1, TimeUnit.SECONDS, new PriorityBlockingQueue<>());
    }

    public TeXngine(final String baseDirPath, final ExecutorService executor) {
        baseDir = new File(baseDirPath);
        this.executor = executor;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void execute(final TeXCommand texCommand) {
        if (debug)
            texCommand.run();
        else
            executor.execute(texCommand);
    }

    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public void finalize() {
        if (!executor.isShutdown())
            executor.shutdownNow();
    }

    public File getBaseDir() {
        return baseDir;
    }

    public CommandFactory getCommandFactory() {
        return new CommandFactory();
    }

    public class CommandFactory {

        private final List<String> commandAndarguments = new ArrayList<>();

        private long priority = 0;

        private CommandFactory() { }

        public CommandFactory setCommandAndarguments(final String... commandAndarguments) {
            this.commandAndarguments.clear();
            this.commandAndarguments.addAll(Arrays.asList(commandAndarguments));

            return this;
        }

        public CommandFactory setCommandAndarguments(final List<String> commandAndarguments) {
            this.commandAndarguments.clear();
            this.commandAndarguments.addAll(commandAndarguments);

            return this;
        }

        public CommandFactory setPriority(long priority) {
            this.priority = priority;

            return this;
        }

        public TeXCommand create() {
            TeXCommand teXCommand = new TeXCommand(TeXngine.this, commandAndarguments);
            teXCommand.setPriority(priority);
            return teXCommand;
        }
    }
}
