package org.texngine;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeXngine {

    public final CommandFactory COMMAND_FACTORY = new CommandFactory(this);

    private final File baseDir;

    // TODO: replace with factory to setup multithreading environment
    public TeXngine(final String baseDirPath) {
        baseDir = new File(baseDirPath);
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
