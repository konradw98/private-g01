package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.GetUsersResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetUsersHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();

        try {
            Parameters parameters = commandRequest.getParameters();
            String skip = parameters.get("skip");
            String top = parameters.get("top");

            String wrongParameters = validateHeaders(skip, top);
            if (!wrongParameters.equals("")) {
                conn.close();
                return new WrongParametersResult(wrongParameters);
            }

            int skipInt = Integer.parseInt(skip) + 1;

            String sql = "SELECT * FROM users WHERE uid BETWEEN ? AND ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, skipInt);
            pstmt.setInt(2, Integer.parseInt(top) + skipInt - 1);
            ResultSet resultSet = pstmt.executeQuery();
            conn.close();

            int uid;
            String name;
            String email;
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
            } else {
                return new GetUsersResult(users);
            }

        } finally {
            conn.close();
        }
    }

    private String validateHeaders(String skip, String top) {
        String wrongParameters = "";
        if (skip == null || Integer.parseInt(skip) < 0) {
            wrongParameters += "skip ";
        }
        if (top == null || Integer.parseInt(top) < 0) {
            wrongParameters += " top";
        }
        return wrongParameters;
    }

}
