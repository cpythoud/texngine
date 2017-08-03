package org.texngine;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TeXngine {

    public final CommandFactory COMMAND_FACTORY = new CommandFactory(this);

    private final File baseDir;
    private final ThreadPoolExecutor executor;

    public TeXngine(final String baseDirPath, final int threadCount) {
        baseDir = new File(baseDirPath);
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
    }

    public void execute(final TeXCommand texCommand) {
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

    public static final String NON_STOP_MODE_OPTION = "-interaction=nonstopmode";
    public static final String BATCH_MODE_OPTION    = "-interaction=batchmode";

    public static class CommandFactory {

        private final TeXngine texngine;

        private final List<String> commandAndarguments = new ArrayList<>();

        private CommandFactory(final TeXngine texngine) {
            this.texngine = texngine;
        }

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

        public TeXCommand create() {
            return new TeXCommand(texngine, commandAndarguments);
        }
    }
}
