package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class GetRoutesHandler implements CommandHandler {
    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {

        Connection conn = commandRequest.getDataSource().getConnection();

        //deleting student that does not exist in table yet (value 0 means no rows were affected)
        String sql = "SELECT * FROM routes";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt.executeQuery()));
        conn.close();
        return optional;
    }
}
