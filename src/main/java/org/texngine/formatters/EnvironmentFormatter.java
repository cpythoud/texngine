package org.texngine.formatters;

import org.beanmaker.v2.util.Strings;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentFormatter extends CommonMacroFormatter {

    private final List<CommonMacroFormatter> content = new ArrayList<>();

    public EnvironmentFormatter(String name, String optionalParameter, String... parameters) {
        super(name, optionalParameter, parameters);
    }

    public EnvironmentFormatter(String name, String optionalParameter, List<String> parameters) {
        super(name, optionalParameter, parameters);
    }

    public void clearContent() {
        content.clear();
    }

    public void addContent(CommonMacroFormatter contentPiece) {
        content.add(contentPiece);
    }

    @Override
    public String toString() {
        return println(0);
    }

    @Override
    public String print(int tabs) {
        return println(tabs);
    }

    @Override
    public String println(int tabs) {
        StringBuilder buf = new StringBuilder();

        buf.append(Strings.repeatString("\t", tabs))
                .append("\\begin{")
                .append(name)
                .append("}")
                .append(formatParameters())
                .append("\n");

        content.forEach(contentPiece -> buf.append(contentPiece.println(tabs + 1)));

        buf.append(Strings.repeatString("\t", tabs))
                .append("\\end{")
                .append(name)
                .append("}\n");

        return buf.toString();
    }

}
