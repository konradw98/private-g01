package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.PostResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostUserHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Parameters parameters = commandRequest.getParameters();
        String name = parameters.get("name");
        String email = parameters.get("email");

        String wrongParameters = checkParameters(name, email);

        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        Connection conn = commandRequest.getDataSource().getConnection();
        try {
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
            } else {
                return new WrongParametersResult(wrongParameters);
            }

        } finally {
            conn.close();
        }
    }

    private String checkParameters(String name, String email) {
        String wrongParameters = "";
        if (name == null) {
            wrongParameters += "name ";
        }

        if (email == null) {
            wrongParameters += "email ";
        }

        return wrongParameters;
    }
}
