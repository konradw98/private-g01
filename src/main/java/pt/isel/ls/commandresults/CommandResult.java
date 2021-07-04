package pt.isel.ls.commandresults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.http.servlets.AppServlet;

public interface CommandResult {
    Logger log = LoggerFactory.getLogger(CommandResult.class);

    boolean results(boolean http);

    String generateResults(boolean http);
}
