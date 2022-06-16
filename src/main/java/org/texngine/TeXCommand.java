package org.texngine;

import java.nio.file.Path;

public interface TeXCommand extends Runnable {

    default void execute(String dir) {
        execute(Path.of(dir));
    }

    default void execute(int passes, String dir) {
        execute(passes, Path.of(dir));
    }

    default void execute(
            int passes,
            String dir,
            PreProcessor preProcessor,
            PostProcessor postProcessor,
            ErrorProcessor errorProcessor
    ) {
        execute(passes, Path.of(dir), preProcessor, postProcessor, errorProcessor);
    }

    void execute(Path dir);

    void execute(int passes, Path dir);

    void execute(
            int passes,
            Path dir,
            PreProcessor preProcessor,
            PostProcessor postProcessor,
            ErrorProcessor errorProcessor
    );

}
