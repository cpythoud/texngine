package org.texngine;

public interface TeXCommand extends Runnable {

    void execute(String subDirectory);

    void execute(int passes, String subDirectory);

    void execute(
            int passes,
            String subDirectory,
            PreProcessor preProcessor,
            PostProcessor postProcessor,
            ErrorProcessor errorProcessor
    );

}
