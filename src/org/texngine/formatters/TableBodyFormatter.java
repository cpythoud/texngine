package org.texngine.formatters;

import org.dbbeans.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableBodyFormatter {

    private static final String COMMON_VERTICAL_SEPARATOR = "\\hline";

    private static final String DEFAULT_HORIZONTAL_SEPARATOR  = "&";
    private static final String DEFAULT_END_OF_LINE_SEPARATOR = "\\\\";

    private final int columns;
    private final String verticalSeparator;
    private final String horizontalSeparator;
    private final String endOfLineSeparator;

    private final List<List<String>> lines = new ArrayList<>();

    public static TableBodyFormatter getFormatterWithLineSeparations(final int columns) {
        return new TableBodyFormatter(columns, COMMON_VERTICAL_SEPARATOR);
    }

    public TableBodyFormatter(final int columns)
    {
        this(columns, null, DEFAULT_HORIZONTAL_SEPARATOR, DEFAULT_END_OF_LINE_SEPARATOR);
    }

    public TableBodyFormatter(final int columns, final String verticalSeparator)
    {
        this(columns, verticalSeparator, DEFAULT_HORIZONTAL_SEPARATOR, DEFAULT_END_OF_LINE_SEPARATOR);
    }

    public TableBodyFormatter(final int columns, final String verticalSeparator, final String horizontalSeparator)
    {
        this(columns, verticalSeparator, horizontalSeparator, DEFAULT_END_OF_LINE_SEPARATOR);
    }

    public TableBodyFormatter(
            final int columns,
            final String verticalSeparator,
            final String horizontalSeparator,
            final String endOfLineSeparator)
    {
        this.columns = columns;
        this.verticalSeparator = verticalSeparator;
        this.horizontalSeparator = horizontalSeparator;
        this.endOfLineSeparator = endOfLineSeparator;
    }

    public void addLine(final String... columnData) {
        addLine(Arrays.asList(columnData));
    }

    public void addLine(final List<String> columnData) {
        if (columnData.size() != columns)
            throw new IllegalArgumentException("Wrong number of elements: " + columnData.size()
                    + " for table with " + columns + " columns");

        final List<String> line = new ArrayList<>();
        line.addAll(columnData);
        lines.add(line);
    }

    public String print(final int tabCount) {
        if (tabCount < 0)
            throw new IllegalArgumentException("Tab number should be 0 or positive");
        if (lines.size() == 0)
            throw new IllegalArgumentException("No line to print");

        final StringBuilder tableBody = new StringBuilder();
        final String tabs = Strings.repeatString("\t", tabCount);

        lines.forEach(line -> {
            tableBody.append(tabs);
            int index = 0;
            for (String column: line) {
                ++index;
                tableBody.append(column);
                if (index < columns)
                    tableBody.append(horizontalSeparator);
                else if (verticalSeparator != null)
                    tableBody.append(endOfLineSeparator);
            }
            tableBody.append("\n");
            if (verticalSeparator != null)
                tableBody.append(tabs).append(verticalSeparator).append("\n");
        });

        return tableBody.toString();
    }

    @Override
    public String toString() {
        return print(0);
    }
}
