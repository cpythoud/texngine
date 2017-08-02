package org.texngine.formatters;

import org.dbbeans.util.Strings;

import java.util.List;

public class CommandFormatter extends CommonMacroFormatter {

    public CommandFormatter(final String name, final String optionalParameter, final String... parameters) {
        super(name, optionalParameter, parameters);
    }

    public CommandFormatter(final String name, final String optionalParameter, final List<String> parameters) {
        super(name, optionalParameter, parameters);
    }

    @Override
    public String toString() {
        return "\\" + name + formatParameters();
    }

    @Override
    public String print(final int tabs) {
        return Strings.repeatString("\t", tabs) + toString();
    }

    @Override
    public String println(final int tabs) {
        return print(tabs) + "\n";
    }
}
