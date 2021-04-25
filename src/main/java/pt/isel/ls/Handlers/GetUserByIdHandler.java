package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetUserByIdHandler implements CommandHandler {

    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        ArrayList<String> parameters = commandRequest.getPathParameters();
        int uid = Integer.parseInt(parameters.get(0));

        String sql1 = "SELECT MAX(uid) FROM users";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();

        if (resultSet.getInt(1) < uid || uid < 1) {
            System.out.println("Wrong parameter: uid = " + uid);
            conn.close();
            return Optional.empty();
        }

        String sql = "SELECT * FROM users WHERE uid=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, uid);
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt.executeQuery()));
        conn.close();

        return optional;
    }
}
