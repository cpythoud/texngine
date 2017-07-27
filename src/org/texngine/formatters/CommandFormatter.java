package org.texngine.formatters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CommandFormatter {

    private final String name;
    private final String optionalParameter;
    private final List<String> parameters;

    public CommandFormatter(final String name, final String optionalParameter, final String... parameters) {
        this(name, optionalParameter, Arrays.asList(parameters));
    }

    public CommandFormatter(final String name, final String optionalParameter, final List<String> parameters) {
        this.name = name;
        this.optionalParameter = optionalParameter;
        this.parameters = new ArrayList<>();
        this.parameters.addAll(parameters);
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();

        buf.append("\\").append(name);

        if (optionalParameter != null)
            buf.append("[").append(optionalParameter).append("]");

        parameters.forEach(parameter -> buf.append("{").append(parameter).append("}"));

        return buf.toString();
    }

    public String print(final int tabs) {
        final StringBuilder buf = new StringBuilder();

        IntStream.range(0, tabs).forEach(i -> buf.append("\t"));
        buf.append(toString());

        return buf.toString();
    }

    public String println(final int tabs) {
        return print(tabs) + "\n";
    }
}
