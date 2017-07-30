package org.texngine.formatters;

public class FootnoteFormatter {

    public static String getSimpleFootnote(final String text) {
        return "\\,\\footnote{" + TextFormatter.escapeCharsInCommandArguments(text) + "}";
    }
}
