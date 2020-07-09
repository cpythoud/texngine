package org.texngine.formatters;

import org.dbbeans.util.Files;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class TeXFile {

    private final String filepath;
    private final List<String> lines = new ArrayList<>();

    private boolean fileWritten = false;

    public TeXFile(String filepath) {
        this.filepath = filepath;
    }

    public boolean isFileWritten() {
        return fileWritten;
    }

    public TeXFile addMacro(CommonMacroFormatter macro) {
        addMacro(macro, 0);

        return this;
    }

    public TeXFile addMacro(CommonMacroFormatter macro, int tabs) {
        if (fileWritten)
            throw new IllegalStateException("File has already been written");

        lines.add(macro.print(tabs));

        return this;
    }

    public TeXFile addRawLine(String line) {
        if (line == null)
            throw new NullPointerException("line cannot be null");
        if (fileWritten)
            throw new IllegalStateException("File has already been written");

        lines.add(line);

        return this;
    }

    public TeXFile addEmptyLine() {
        if (fileWritten)
            throw new IllegalStateException("File has already been written");

        lines.add("\n");

        return this;
    }

    public void writeFile() {
        StringBuilder contents = new StringBuilder();
        for (String line: lines)
            contents.append(line).append("\n");

        Files.write(contents.toString(), new File(filepath));
        fileWritten = true;
    }

    public void writeEmptyFile() {
        Files.createEmptyFile(new File(filepath));
        fileWritten = true;
    }
}
