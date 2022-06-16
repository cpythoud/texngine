package org.texngine;

@FunctionalInterface
public interface ErrorProcessor  {

    void processCompilationErrors(String logFileContent);

}
