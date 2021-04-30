package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;

import java.sql.SQLException;
import java.util.Optional;

public interface CommandHandler {
    CommandResult execute(CommandRequest commandRequest) throws SQLException;
}
