package org.texngine;

import java.io.File;

@FunctionalInterface
public interface ErrorProcessor  {

    void processCompilationErrors(final File logFile);
}
