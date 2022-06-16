package org.texngine;

import java.io.File;

public interface TeXngine {

    void setDebug(boolean debug);

    void execute(TeXCommandImpl texCommand);

    void shutdown();

    File getBaseDir();

    TeXCommandFactory getCommandFactory();

}
