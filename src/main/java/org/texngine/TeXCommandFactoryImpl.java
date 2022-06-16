package org.texngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeXCommandFactoryImpl implements TeXCommandFactory {

    private final List<String> commandAndarguments = new ArrayList<>();

    private long priority = 0;

    @Override
    public TeXCommandFactory setCommandAndArguments(String... commandAndarguments) {
        this.commandAndarguments.clear();
        this.commandAndarguments.addAll(Arrays.asList(commandAndarguments));

        return this;
    }

    @Override
    public TeXCommandFactory setCommandAndArguments(List<String> commandAndarguments) {
        this.commandAndarguments.clear();
        this.commandAndarguments.addAll(commandAndarguments);

        return this;
    }

    @Override
    public TeXCommandFactory setPriority(long priority) {
        this.priority = priority;

        return this;
    }

    @Override
    public TeXCommand create(TeXngineImpl texngine) {
        TeXCommandImpl teXCommand = new TeXCommandImpl(texngine, commandAndarguments);
        teXCommand.setPriority(priority);
        return teXCommand;
    }

}