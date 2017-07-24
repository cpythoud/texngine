package org.texngine;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class TeXCommand {

    private static final String STD_OUT_LOG_FILE = "texngine.log";

    private final TeXngine teXngine;
    private final ErrorProcessor errorProcessor;
    private final List<String> commandAndarguments = new ArrayList<>();

    TeXCommand(final TeXngine teXngine, final ErrorProcessor errorProcessor, final List<String> commandAndarguments) {
        this.teXngine = teXngine;
        this.errorProcessor = errorProcessor;
        this.commandAndarguments.addAll(commandAndarguments);
    }

    public void execute(final String subDirectory) {
        execute(1, subDirectory, null, null);
    }

    public void execute(final int passes, final String subDirectory) {
        execute(passes, subDirectory, null, null);
    }

    public void execute(
            final int passes,
            final String subDirectory,
            final PreProcessor preProcessor,
            final PostProcessor postProcessor)
    {
        if (preProcessor != null)
            preProcessor.doPreProcessing();

        final ProcessBuilder processBuilder = new ProcessBuilder(commandAndarguments);

        final File executionDirectory = new File(teXngine.getBaseDir(), subDirectory);
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

        if (postProcessor != null)
            postProcessor.doPostProcessing();  // TODO: include error management here
    }
}
