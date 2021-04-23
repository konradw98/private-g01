package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.SQLException;
import java.util.Optional;

public interface CommandHandler {
    Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException;
}
