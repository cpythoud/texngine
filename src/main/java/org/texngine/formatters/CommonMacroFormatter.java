package org.texngine.formatters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class CommonMacroFormatter {

    final String name;
    final String optionalParameter;
    final List<String> parameters;

    public CommonMacroFormatter(String name, String optionalParameter, String... parameters) {
        this(name, optionalParameter, Arrays.asList(parameters));
    }

    public CommonMacroFormatter(String name, String optionalParameter, List<String> parameters) {
        this.name = name;
        this.optionalParameter = optionalParameter;
        this.parameters = new ArrayList<>();
        this.parameters.addAll(parameters);
    }

    public abstract String print(int tabs);
    public abstract String println(int tabs);

    String formatParameters() {
        StringBuilder buf = new StringBuilder();

        if (optionalParameter != null)
            buf.append("[").append(optionalParameter).append("]");

        parameters.forEach(parameter -> buf.append("{").append(parameter).append("}"));

        return buf.toString();
    }

}
