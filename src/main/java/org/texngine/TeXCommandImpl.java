package org.texngine;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeXCommandImpl extends TeXPriorityTask implements TeXCommand {

    private static final String STD_OUT_LOG_FILE = "texngine.log";

    private final TeXngine teXngine;
    private final List<String> commandAndarguments = new ArrayList<>();

    TeXCommandImpl(TeXngine teXngine, List<String> commandAndarguments) {
        this.teXngine = teXngine;
        this.commandAndarguments.addAll(commandAndarguments);
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

        teXngine.execute(this);
    }

    @Override
    public void run() {
        if (passes == 0)
            throw new IllegalStateException("Command has not been passed any parameter");

        if (preProcessor != null)
            preProcessor.doPreProcessing();

        ProcessBuilder processBuilder = new ProcessBuilder(commandAndarguments);
        //System.out.println("Arguments: " + Strings.concatWithSeparator(" ", commandAndarguments));
        // TODO: various commented System.out.println() must make their way into a log file

        File executionDirectory = dir.toFile();
        //System.out.println("Execution directory: " + executionDirectory.getPath());
        processBuilder.directory(executionDirectory);

        File logFile = new File(executionDirectory, STD_OUT_LOG_FILE);
        if (logFile.exists() && !logFile.delete())
            throw new ProcessingError("Could not delete log file: " + logFile.getPath());
        try {
            //System.out.println("Creating file: " + logFile.getPath());
            if (!logFile.createNewFile())
                throw new ProcessingError("Could not create log file: " + logFile.getPath());
        } catch (IOException ioex) {
            throw new ProcessingError(ioex);
        }
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));

        try {
            for (int i = 0; i < passes; ++i) {
                //System.out.println("Launching process #" + i);
                Process process = processBuilder.start();
                int exitValue = process.waitFor();
                //System.out.println(exitValue);
            }
        } catch (IOException ioex) {
            //System.out.println("IOException while processing TeX file");
            throw new ProcessingError(ioex);
        } catch (InterruptedException intex) {
            throw new ProcessingError(intex); // TODO: must be integrated into thread management later
        }

        String logFileContent;
        try {
            logFileContent = Files.readString(logFile.toPath());
        } catch (IOException ioex) {
            throw new ProcessingError(ioex);
        }
        if (containsErrors(logFileContent)) {
            if (errorProcessor != null)
                errorProcessor.processCompilationErrors(logFileContent);
        } else if (postProcessor != null)
            postProcessor.doPostProcessing();

        passes = 0;
    }

    private boolean containsErrors(String logFileContent) {
        return Arrays.stream(logFileContent.split("\n"))
                .anyMatch(line -> (line.startsWith("!") || line.startsWith("quiting: ")));
    }

}
