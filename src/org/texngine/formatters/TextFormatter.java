package org.texngine.formatters;

import org.dbbeans.util.Strings;

import java.util.HashMap;
import java.util.Map;

public class TextFormatter {

    public static String processLineFeeds(final String source, final String doubleLFReplacement) {
        final String[] lines = source.split("\n");
        final StringBuilder processedSource = new StringBuilder();

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

    private static final Map<String, String> ESCAPE_CHARS_IN_COMMAND_ARGUMENTS;

    static {
        ESCAPE_CHARS_IN_COMMAND_ARGUMENTS = new HashMap<>();

        ESCAPE_CHARS_IN_COMMAND_ARGUMENTS.put("%", "\\\\%");
        ESCAPE_CHARS_IN_COMMAND_ARGUMENTS.put("\\{", "\\\\{");
        ESCAPE_CHARS_IN_COMMAND_ARGUMENTS.put("}", "\\\\}");
    }

    public static String escapeCharsInCommandArguments(final String source) {
        return Strings.regexReplaceMany(source, ESCAPE_CHARS_IN_COMMAND_ARGUMENTS);
    }
}
