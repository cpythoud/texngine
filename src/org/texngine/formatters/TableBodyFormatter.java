package org.texngine.formatters;

import org.dbbeans.util.Strings;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TableBodyFormatter {

    private static final String COMMON_VERTICAL_SEPARATOR = "\\hline";

    private static final String DEFAULT_HORIZONTAL_SEPARATOR  = "&";
    private static final String DEFAULT_END_OF_LINE_SEPARATOR = "\\\\";

    private final int columns;
    private final String verticalSeparator;
    private final String horizontalSeparator;
    private final String endOfLineSeparator;

    private final List<List<String>> lines = new ArrayList<>();

    private List<List<String>> sortedLines;

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

        sortedLines = null;
    }

    public String print(final int tabCount) {
        final List<List<String>> linesToPrint;
        if (sortedLines == null)
            linesToPrint = lines;
        else
            linesToPrint = sortedLines;

        if (tabCount < 0)
            throw new IllegalArgumentException("Tab number should be 0 or positive");
        if (linesToPrint.size() == 0)
            throw new IllegalArgumentException("No line to print");

        final StringBuilder tableBody = new StringBuilder();
        final String tabs = Strings.repeatString("\t", tabCount);

        linesToPrint.forEach(line -> {
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

    public void sort(final int sortColumn) {
        sort(sortColumn, null);
    }

    public void sort(final int sortColumn, final Locale locale) {
        if (sortColumn < 1 || sortColumn > columns)
            throw new IllegalArgumentException(
                    "No column #" + sortColumn + ". Columns must be > 0 and < " + (columns + 1));

        final List<SortingLine> sortingLines = new ArrayList<>();
        lines.forEach(line ->
                sortingLines.add(new SortingLine(line.get(sortColumn - 1), line, locale)));
        Collections.sort(sortingLines);

        final List<List<String>> sortedLines = new ArrayList<>();
        sortingLines.forEach(sortingLine -> sortedLines.add(sortingLine.line));
        this.sortedLines = sortedLines;
    }

    private static class SortingLine implements Comparable<SortingLine> {
        private final String data;
        private final List<String> line;

        private final Collator collator;

        SortingLine(final String data, final List<String> line, final Locale locale) {
            this.data = data;
            this.line = line;

            if (locale == null)
                collator = null;
            else
                collator = Collator.getInstance(locale);
        }

        @Override
        public int compareTo(final SortingLine sortingLine) {
            if (collator == null)
                return data.compareTo(sortingLine.data);

            return collator.compare(data, sortingLine.data);
        }
    }
}
