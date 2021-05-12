package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.ExitResult;

import java.sql.SQLException;

public class ExitHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        return new ExitResult();
    }
}
