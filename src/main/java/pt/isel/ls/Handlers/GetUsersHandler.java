package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;
import pt.isel.ls.CommandResults.GetUserByIdResult;
import pt.isel.ls.CommandResults.GetUsersResult;
import pt.isel.ls.CommandResults.WrongParametersResult;
import pt.isel.ls.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetUsersHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();

        String sql = "SELECT * FROM users";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        conn.close();

        int uid;
        String name, email;
        User user;
        ArrayList<User> users = new ArrayList<>();

        while (resultSet.next()) {
            uid = resultSet.getInt("uid");
            name = resultSet.getString("name");
            email = resultSet.getString("email");
            user = new User(uid, email, name);
            users.add(user);
        }
        if (users.size() == 0) {
            return new WrongParametersResult();
        } else return new GetUsersResult(users);

        //TODO: think about how to differ empty db and wrong parameters
    }
}
