package org.texngine.formatters;

import org.beanmaker.v2.util.Strings;

import java.util.List;

public class CommandFormatter extends CommonMacroFormatter {

    public CommandFormatter(String name, String optionalParameter, String... parameters) {
        super(name, optionalParameter, parameters);
    }

    public CommandFormatter(String name, String optionalParameter, List<String> parameters) {
        super(name, optionalParameter, parameters);
    }

    @Override
    public String toString() {
        return "\\" + name + formatParameters();
    }

    @Override
    public String print(int tabs) {
        return Strings.repeatString("\t", tabs) + this;
    }

    @Override
    public String println(int tabs) {
        return print(tabs) + "\n";
    }

}
