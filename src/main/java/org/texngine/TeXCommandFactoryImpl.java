package org.texngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeXCommandFactoryImpl implements TeXCommandFactory {

    private final List<String> commandAndArguments = new ArrayList<>();

    private final TeXngine texngine;

    private long priority = 0;

    public TeXCommandFactoryImpl(TeXngine texngine) {
        this.texngine = texngine;
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
        this.priority = priority;

        return this;
    }

    @Override
    public TeXCommand create() {
        TeXCommandImpl teXCommand = new TeXCommandImpl(texngine, commandAndArguments);
        teXCommand.setPriority(priority);
        return teXCommand;
    }

}
