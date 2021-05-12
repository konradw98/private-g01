package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.OptionResult;
import java.sql.SQLException;

public class OptionHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        return new OptionResult();
    }
}
