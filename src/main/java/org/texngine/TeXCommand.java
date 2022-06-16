package org.texngine;

public interface TeXCommand extends Runnable {

    void execute(String dir);

    void execute(int passes, String dir);

    void execute(
            int passes,
            String dir,
            PreProcessor preProcessor,
            PostProcessor postProcessor,
            ErrorProcessor errorProcessor
    );

}
