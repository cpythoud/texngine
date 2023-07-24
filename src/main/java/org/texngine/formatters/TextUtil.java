package org.texngine.formatters;

import org.beanmaker.v2.util.Strings;

import java.util.HashMap;
import java.util.Map;

public class TextUtil {

    public static String doubleLineFeeds(String source, String doubleLFReplacement) {
        String[] lines = source.split("\n");
        StringBuilder processedSource = new StringBuilder();

        boolean prevLineEmpty = false;
        for (String line: lines) {
            if (!Strings.isEmpty(line)) {
                processedSource.append(line).append("\n\n");
                prevLineEmpty = false;
            } else {
                if (!prevLineEmpty) {
                    processedSource.append(doubleLFReplacement).append("\n\n");
                    prevLineEmpty = true;
                }
            }
        }

        return processedSource.toString();
    }

    private static final Map<String, String> ESCAPE_CHARS;

    static {
        ESCAPE_CHARS = new HashMap<>();

        ESCAPE_CHARS.put("%", "\\%");
        ESCAPE_CHARS.put("$", "\\$");
        ESCAPE_CHARS.put("&", "\\&");
        ESCAPE_CHARS.put("#", "\\#");
        ESCAPE_CHARS.put("_", "\\_");
        ESCAPE_CHARS.put("{", "\\{");
        ESCAPE_CHARS.put("}", "\\}");
        ESCAPE_CHARS.put("~", "{\\textasciitilde}");
        ESCAPE_CHARS.put("^", "{\\textasciicircum}");
    }

    public static String escapeChars(String source) {
        String noBackSlash = Strings.replace(source, "\\", "\\\\");
        return Strings.replaceMany(noBackSlash, ESCAPE_CHARS);
    }

}
