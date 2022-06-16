package org.texngine;

import java.util.List;

public interface TeXCommandFactory {

    TeXCommandFactory setCommandAndArguments(String... commandAndarguments);

    TeXCommandFactory setCommandAndArguments(List<String> commandAndarguments);

    TeXCommandFactory setPriority(long priority);

    TeXCommand create();

}
