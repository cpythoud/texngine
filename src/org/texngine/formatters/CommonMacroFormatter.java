package org.texngine.formatters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class CommonMacroFormatter {

    final String name;
    final String optionalParameter;
    final List<String> parameters;

    public CommonMacroFormatter(final String name, final String optionalParameter, final String... parameters) {
        this(name, optionalParameter, Arrays.asList(parameters));
    }

    public CommonMacroFormatter(final String name, final String optionalParameter, final List<String> parameters) {
        this.name = name;
        this.optionalParameter = optionalParameter;
        this.parameters = new ArrayList<>();
        this.parameters.addAll(parameters);
    }

    public abstract String print(final int tabs);
    public abstract String println(final int tabs);

    String formatParameters() {
        final StringBuilder buf = new StringBuilder();

        if (optionalParameter != null)
            buf.append("[").append(optionalParameter).append("]");

        parameters.forEach(parameter -> buf.append("{").append(parameter).append("}"));

        return buf.toString();
    }
}
