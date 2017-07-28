package org.texngine.formatters;

import org.dbbeans.util.Strings;

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
}
