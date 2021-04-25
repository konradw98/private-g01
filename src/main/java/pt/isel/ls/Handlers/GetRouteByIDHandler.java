package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetRouteByIDHandler implements CommandHandler {
    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();

        ArrayList<String> parameters = commandRequest.getPathParameters();

        String sql = "SELECT * FROM routes WHERE rid=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, Integer.parseInt(parameters.get(0)));
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt.executeQuery()));
        conn.close();
        if (!optional.get().getResultSet().next()) {
            System.out.println("Wrong parameter: rid = " + parameters.get(0));
        }
        return optional;
    }
}
