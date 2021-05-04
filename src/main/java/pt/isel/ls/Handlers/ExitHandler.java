package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;

import java.sql.SQLException;

public class ExitHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
       System.exit(0);
       return null;
    }
}
