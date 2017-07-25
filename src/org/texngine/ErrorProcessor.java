package org.texngine;

@FunctionalInterface
public interface ErrorProcessor  {

    void processCompilationErrors(final String logFileContent);
}
