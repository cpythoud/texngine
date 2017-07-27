package org.texngine.formatters;

import org.dbbeans.util.Strings;

import java.util.List;

public class ItemFormatter {

    public static String formatSimpleItems(final List<String> items) {
        return formatSimpleItems(items, 0);
    }

    public static String formatSimpleItems(final List<String> items, final int tabs) {
        final StringBuilder itemBlock = new StringBuilder();

        items.forEach(item -> itemBlock
                .append(Strings.repeatString("\t", tabs))
                .append("\\item ")
                .append(item)
                .append("\n"));

        return itemBlock.toString();
    }
}
