package org.texngine;

import org.beanmaker.v2.util.Strings;
import org.beanmaker.v2.util.logging.Logger;

import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;

public class MockTeXCommand implements TeXCommand {

    private final TeXngine teXngine;
    private final List<String> commandAndArguments = new ArrayList<>();
    private final Logger logger;

    MockTeXCommand(TeXngine teXngine, List<String> commandAndArguments, Logger logger) {
        this.teXngine = teXngine;
        this.commandAndArguments.addAll(commandAndArguments);
        this.logger = logger;
    }

    private int passes = 0;
    private Path dir;
    private PreProcessor preProcessor;
    private PostProcessor postProcessor;
    private ErrorProcessor errorProcessor;

    public void execute(Path dir) {
        execute(1, dir, null, null, null);
    }

    public void execute(int passes, Path dir) {
        execute(passes, dir, null, null, null);
    }

    public void execute(
            int passes,
            Path dir,
            PreProcessor preProcessor,
            PostProcessor postProcessor,
            ErrorProcessor errorProcessor)
    {
        this.passes = passes;
        this.dir = dir;
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
        this.errorProcessor = errorProcessor;

        logger.info("Command execution requested: passes = " + passes + ", in dir " + dir.toFile().getAbsolutePath()
                + " [" + getProcessorsInfo() + "]");

        teXngine.execute(this);
    }

    private String getProcessorsInfo() {
        return "Preprocessor = " + (preProcessor == null ? "---" : preProcessor.getClass().getCanonicalName())
                + ", Postprocessor = " + (postProcessor == null ? "---" : postProcessor.getClass().getCanonicalName())
                + ", Error processor = " + (errorProcessor == null ? "---" : errorProcessor.getClass().getCanonicalName());
    }

    @Override
    public void run() {
        logger.info("Running TeX Command (" + passes + " times): " + Strings.concatWithSeparator(" ", commandAndArguments)
                + " [in directory: " + dir.toFile().getAbsolutePath() + "]");
        logger.info("MockTeXCommand: nothing happens; processors will be invoked if not null");

        if (preProcessor != null) {
            logger.info("Executing preprocessor " + preProcessor.getClass().getCanonicalName());
            preProcessor.doPreProcessing();
        }

        if (postProcessor != null) {
            logger.info("Executing postprocessor " + postProcessor.getClass().getCanonicalName());
            postProcessor.doPostProcessing();
        }

        if (errorProcessor != null) {
            logger.info("Executing error processor " + errorProcessor.getClass().getCanonicalName());
            errorProcessor.processCompilationErrors("logfile content");
        }
    }

}
