package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.SQLException;

public interface CommandHandler {
    CommandResult execute(CommandRequest commandRequest) throws SQLException;
}
