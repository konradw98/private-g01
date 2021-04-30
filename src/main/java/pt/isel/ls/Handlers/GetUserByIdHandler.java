package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;
import pt.isel.ls.CommandResults.GetUserByIdResult;
import pt.isel.ls.CommandResults.WrongParametersResult;
import pt.isel.ls.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetUserByIdHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        ArrayList<String> parameters = commandRequest.getPathParameters();

        String sql = "SELECT * FROM users WHERE uid=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, Integer.parseInt(parameters.get(0)));
        ResultSet resultSet = pstmt.executeQuery();
        conn.close();
        if (resultSet.next()) {
            int uid = resultSet.getInt("uid");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            User user = new User(uid, email, name);
            return new GetUserByIdResult(user);
        } else return new WrongParametersResult();
    }
}
