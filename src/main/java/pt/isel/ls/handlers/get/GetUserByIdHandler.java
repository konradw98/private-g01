package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.getresult.GetUserByIdResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserByIdHandler extends GetHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        String stringUid = commandRequest.getPathParameters().get("uid");
        String wrongParameters = validatePathParameters(stringUid);
        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            String sql = "SELECT * FROM users WHERE uid=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(stringUid));
            ResultSet resultSet = pstmt.executeQuery();
            conn.close();

            if (resultSet.next()) {
                int uid = resultSet.getInt("uid");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                User user = new User(uid, email, name);
                return new GetUserByIdResult(user, commandRequest.getHeaders());
            } else {
                return new WrongParametersResult();
            }
        } finally {
            conn.close();
        }
    }

    private String validatePathParameters(String uid) {
        String wrongParameters = "";
        if (uid == null || Integer.parseInt(uid) < 1) {
            wrongParameters += "uid ";
        }
        return wrongParameters;
    }
}
