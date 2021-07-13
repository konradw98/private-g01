package pt.isel.ls.commandresults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface CommandResult {
    Logger log = LoggerFactory.getLogger(CommandResult.class);

    boolean results(boolean http);

    String generateResults(boolean http);
}
