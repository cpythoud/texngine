package org.texngine.formatters;

import org.beanmaker.v2.util.Strings;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class TextFormatter {

    public static String processLineFeeds(String source, String doubleLFReplacement) {
        return TextUtil.doubleLineFeeds(source, doubleLFReplacement);
    }

    private static final Map<String, String> ESCAPE_CHARS_IN_COMMAND_ARGUMENTS;

    static {
        ESCAPE_CHARS_IN_COMMAND_ARGUMENTS = new HashMap<>();

        ESCAPE_CHARS_IN_COMMAND_ARGUMENTS.put("%", "\\\\%");
    }

    public static String escapeCharsInCommandArguments(String source) {
        return Strings.regexReplaceMany(source, ESCAPE_CHARS_IN_COMMAND_ARGUMENTS);
    }

}
