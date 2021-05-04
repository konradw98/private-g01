package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;
import pt.isel.ls.CommandResults.PostResult;
import pt.isel.ls.CommandResults.WrongParametersResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PostUserHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();

        try {

            String name = "";
            String email = "";

            for (String param : commandRequest.getParameters()) {
                if (param.contains("name")) name = param.substring(5).replace('+', ' ');
                else if (param.contains("email")) email = param.substring(6).replace('+', ' ');
            }

            String wrongParameters = checkParameters(name, email);
            if (!wrongParameters.equals("")) {
                conn.close();
                return new WrongParametersResult(wrongParameters);
            }

            String sql = "INSERT INTO users(name, email) values(?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();

            String sql1 = "SELECT MAX(uid) FROM users";
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            ResultSet resultSet = pstmt1.executeQuery();
            conn.close();

            if (resultSet.next()) {
                int uid = resultSet.getInt("max");
                return new PostResult(uid, "uid");
            } else return new WrongParametersResult(wrongParameters);

        } finally {
            conn.close();
        }
    }

    private String checkParameters(String name, String email) {
        String wrongParameters = "";
        if (name.equals("")) {
            wrongParameters += " name";
        }

        if (email.equals("")) {
            wrongParameters += " email";
        }

        return wrongParameters;
    }
}
