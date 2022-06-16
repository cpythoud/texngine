package org.texngine.formatters;

public class FootnoteFormatter {

    public static String getSimpleFootnote(String text) {
        return "\\,\\footnote{" + TextUtil.escapeChars(text) + "}";
    }

}
