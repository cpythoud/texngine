package org.texngine;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockTeXCommandFactory implements TeXCommandFactory {

    private final List<String> commandAndArguments = new ArrayList<>();

    private final TeXngine texngine;
    private final Logger logger;

    public MockTeXCommandFactory(TeXngine texngine, Logger logger) {
        this.texngine = texngine;
        this.logger = logger;
    }

    @Override
    public TeXCommandFactory setCommandAndArguments(String... commandAndarguments) {
        this.commandAndArguments.clear();
        this.commandAndArguments.addAll(Arrays.asList(commandAndarguments));

        return this;
    }

    @Override
    public TeXCommandFactory setCommandAndArguments(List<String> commandAndarguments) {
        this.commandAndArguments.clear();
        this.commandAndArguments.addAll(commandAndarguments);

        return this;
    }

    @Override
    public TeXCommandFactory setPriority(long priority) {
        return this;
    }

    @Override
    public TeXCommand create() {
        return new MockTeXCommand(texngine, commandAndArguments, logger);
    }

}
