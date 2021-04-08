package pt.isel.ls;

import java.sql.SQLException;

public interface CommandHandler {
    CommandResult execute(CommandRequest commandRequest) throws SQLException;
}
