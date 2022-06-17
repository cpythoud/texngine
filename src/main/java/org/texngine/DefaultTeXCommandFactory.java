package org.texngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultTeXCommandFactory implements TeXCommandFactory {

    private final List<String> commandAndArguments = new ArrayList<>();

    private final TeXngine texngine;

    private long priority = 0;

    public DefaultTeXCommandFactory(TeXngine texngine) {
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
        DefaultTeXCommand teXCommand = new DefaultTeXCommand(texngine, commandAndArguments);
        teXCommand.setPriority(priority);
        return teXCommand;
    }

}
