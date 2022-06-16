package org.texngine;

import org.beanmaker.v2.util.Strings;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeXCommand extends TeXPriorityTask {

    private static final String STD_OUT_LOG_FILE = "texngine.log";

    private final TeXngine teXngine;
    private final List<String> commandAndarguments = new ArrayList<>();

    TeXCommand(TeXngine teXngine, List<String> commandAndarguments) {
        this.teXngine = teXngine;
        this.commandAndarguments.addAll(commandAndarguments);
    }

    private int passes = 0;
    private String subDirectory;
    private PreProcessor preProcessor;
    private PostProcessor postProcessor;
    private ErrorProcessor errorProcessor;

    public void execute(String subDirectory) {
        execute(1, subDirectory, null, null, null);
    }

    public void execute(int passes, String subDirectory) {
        execute(passes, subDirectory, null, null, null);
    }

    public void execute(
            int passes,
            String subDirectory,
            PreProcessor preProcessor,
            PostProcessor postProcessor,
            ErrorProcessor errorProcessor)
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

        ProcessBuilder processBuilder = new ProcessBuilder(commandAndarguments);
        System.out.println("Arguments: " + Strings.concatWithSeparator(" ", commandAndarguments));

        File executionDirectory = new File(teXngine.getBaseDir(), subDirectory);
        System.out.println("Execution directory: " + executionDirectory.getPath());
        processBuilder.directory(executionDirectory);

        File logFile = new File(executionDirectory, STD_OUT_LOG_FILE);
        if (logFile.exists() && !logFile.delete())
            throw new ProcessingError("Could not delete log file: " + logFile.getPath());
        try {
            System.out.println("Creating file: " + logFile.getPath());
            if (!logFile.createNewFile())
                throw new ProcessingError("Could not create log file: " + logFile.getPath());
        } catch (IOException ioex) {
            throw new ProcessingError(ioex);
        }
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));

        try {
            for (int i = 0; i < passes; ++i) {
                System.out.println("Launching process #" + i);
                Process process = processBuilder.start();
                int exitValue = process.waitFor();
                System.out.println(exitValue);
            }
        } catch (IOException ioex) {
            System.out.println("IOException while processing TeX file");
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
