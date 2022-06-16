package org.texngine.formatters;

public class SimpleDataCommandFormater extends CommandFormatter {

    public SimpleDataCommandFormater(String command, String data) {
        super("newcommand", null, "\\" + command, data);
    }

}
