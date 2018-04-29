package org.texngine;

import org.dbbeans.util.Files;
import org.dbbeans.util.Strings;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeXCommand extends TeXPriorityTask {

    private static final String STD_OUT_LOG_FILE = "texngine.log";

    private final TeXngine teXngine;
    private final List<String> commandAndarguments = new ArrayList<>();

    TeXCommand(final TeXngine teXngine, final List<String> commandAndarguments) {
        this.teXngine = teXngine;
        this.commandAndarguments.addAll(commandAndarguments);
    }

    private int passes = 0;
    private String subDirectory;
    private PreProcessor preProcessor;
    private PostProcessor postProcessor;
    private ErrorProcessor errorProcessor;

    public void execute(final String subDirectory) {
        execute(1, subDirectory, null, null, null);
    }

    public void execute(final int passes, final String subDirectory) {
        execute(passes, subDirectory, null, null, null);
    }

    public void execute(
            final int passes,
            final String subDirectory,
            final PreProcessor preProcessor,
            final PostProcessor postProcessor,
            final ErrorProcessor errorProcessor)
    {
        this.passes = passes;
        this.subDirectory = subDirectory;
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
        this.errorProcessor = errorProcessor;

        teXngine.execute(this);
    }

    @Override
    public void run() {
        if (passes == 0)
            throw new IllegalStateException("Command has not been passed any parameter");

        if (preProcessor != null)
            preProcessor.doPreProcessing();

        final ProcessBuilder processBuilder = new ProcessBuilder(commandAndarguments);
        System.out.println("Arguments: " + Strings.concatWithSeparator(" ", commandAndarguments));

        final File executionDirectory = new File(teXngine.getBaseDir(), subDirectory);
        System.out.println("Execution directory: " + executionDirectory.getPath());
        processBuilder.directory(executionDirectory);

        final File logFile = new File(executionDirectory, STD_OUT_LOG_FILE);
        if (logFile.exists() && !logFile.delete())
            throw new ProcessingError("Could not delete log file: " + logFile.getPath());
        try {
            System.out.println("Creating file: " + logFile.getPath());
            if (!logFile.createNewFile())
                throw new ProcessingError("Could not create log file: " + logFile.getPath());
        } catch (final IOException ioex) {
            throw new ProcessingError(ioex);
        }
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));

        try {
            for (int i = 0; i < passes; ++i) {
                System.out.println("Launching process #" + i);
                final Process process = processBuilder.start();
                final int exitValue = process.waitFor();
                System.out.println(exitValue);
            }
        } catch (final IOException ioex) {
            System.out.println("IOException while processing TeX file");
            throw new ProcessingError(ioex);
        } catch (final InterruptedException intex) {
            throw new ProcessingError(intex); // TODO: must be integrated into thread management later
        }

        final String logFileContent = Files.read(logFile);
        if (containsErrors(logFileContent)) {
            if (errorProcessor != null)
                errorProcessor.processCompilationErrors(logFileContent);
        } else if (postProcessor != null)
            postProcessor.doPostProcessing();

        passes = 0;
    }

    private boolean containsErrors(final String logFileContent) {
        return Arrays.stream(logFileContent.split("\n"))
                .anyMatch(line -> (line.startsWith("!") || line.startsWith("quiting: ")));
    }
}
