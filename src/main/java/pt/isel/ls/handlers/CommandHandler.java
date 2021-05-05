package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import java.sql.SQLException;

public interface CommandHandler {
    CommandResult execute(CommandRequest commandRequest) throws SQLException;
}
