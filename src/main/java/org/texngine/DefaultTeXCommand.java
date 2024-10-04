package org.texngine;

import org.beanmaker.v2.util.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultTeXCommand extends TeXPriorityTask implements TeXCommand {

    private static final String STD_OUT_LOG_FILE = "texngine.log";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TeXngine teXngine;
    private final List<String> commandAndArguments = new ArrayList<>();

    DefaultTeXCommand(TeXngine teXngine, List<String> commandAndArguments) {
        this.teXngine = teXngine;
        this.commandAndArguments.addAll(commandAndArguments);
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
        if (passes < 1 || passes > 10)
            throw new IllegalArgumentException("Illegal number of passes requested. Must be between 1 and 10. Requested: " + passes);

        this.passes = passes;
        this.dir = dir;
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
        this.errorProcessor = errorProcessor;

        teXngine.execute(this);
    }

    @Override
    public void run() {
        if (preProcessor != null)
            preProcessor.doPreProcessing();

        ProcessBuilder processBuilder = new ProcessBuilder(commandAndArguments);
        logger.trace("Arguments: {}", printCommand());

        File executionDirectory = dir.toFile();
        logger.trace("Execution directory: {}", executionDirectory);
        processBuilder.directory(executionDirectory);

        File logFile = new File(executionDirectory, STD_OUT_LOG_FILE);
        if (logFile.exists() && !logFile.delete())
            throw new ProcessingError("Could not delete log file: " + logFile.getPath());
        try {
            logger.debug("Creating file: {}", logFile);
            if (!logFile.createNewFile())
                throw new ProcessingError("Could not create log file: " + logFile.getPath());
        } catch (IOException ioex) {
            throw new ProcessingError(ioex);
        }
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));

        try {
            for (int i = 0; i < passes; ++i) {
                logger.debug("Launching process #{} for command: {}", i, this);
                Process process = processBuilder.start();
                int exitValue = process.waitFor();
                logger.debug("Process finished with exit value {} for command: {}", exitValue, this);
            }
        } catch (IOException ioex) {
            logger.error("IOException while processing TeX files", ioex);
            throw new ProcessingError(ioex);
        } catch (InterruptedException intex) {
            logger.error("InterruptedException while processing TeX files", intex);
            throw new ProcessingError(intex); // TODO: must be integrated into thread management
        }

        String logFileContent;
        try {
            logFileContent = Files.readString(logFile.toPath());
        } catch (IOException ioex) {
            logger.error("IOException while reading log file", ioex);
            throw new ProcessingError(ioex);
        }
        if (containsErrors(logFileContent)) {

            if (errorProcessor != null)
                errorProcessor.processCompilationErrors(logFileContent);
        } else if (postProcessor != null)
            postProcessor.doPostProcessing();
    }

    private boolean containsErrors(String logFileContent) {
        return Arrays.stream(logFileContent.split("\n"))
                .anyMatch(line -> (line.startsWith("!") || line.startsWith("quiting: ")));
    }

    @Override
    public String printCommand() {
        return Strings.concatWithSeparator(" ", commandAndArguments);
    }

    @Override
    public String toString() {
        return "[" + printCommand() + "] in directory: " + dir.toFile().getAbsolutePath();
    }

}
