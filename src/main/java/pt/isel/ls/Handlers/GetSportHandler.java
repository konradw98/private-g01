package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class GetSportHandler implements CommandHandler {


    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();

        String sql = "SELECT * FROM sports";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt.executeQuery()));
        conn.close();
        return optional;
    }
}
