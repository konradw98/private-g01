package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.commandresults.getresult.GetNumUserActivitiesByIdResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetNumUserActivitiesHandler extends GetHandler implements CommandHandler {
    @Override

    public CommandResult execute(CommandRequest commandRequest) throws SQLException {

        String stringUid = commandRequest.getPathParameters().get("uid");
        String wrongParameters = validatePathParameters(stringUid);

        Headers headers = commandRequest.getHeaders();
        wrongParameters += validateHeaders(headers);

        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        try (Connection conn = commandRequest.getDataSource().getConnection()) {
            String sqlCountActivities = "SELECT COUNT(*) FROM activities";
            PreparedStatement pstmt = conn.prepareStatement(sqlCountActivities);
            ResultSet resultSet = pstmt.executeQuery();
            int count = 1;

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count == 0) {
                return new EmptyTableResult("activities");
            }

            String sqlSelectUser = "SELECT * FROM users WHERE uid = ?";
            pstmt = conn.prepareStatement(sqlSelectUser);
            pstmt.setInt(1, Integer.parseInt(stringUid));
            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                int uid = resultSet.getInt("uid");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                User user = new User(uid, email, name);

                String sqlCountUserActivities = "SELECT COUNT(*) FROM activities WHERE uid=?";
                pstmt = conn.prepareStatement(sqlCountUserActivities);
                pstmt.setInt(1, Integer.parseInt(stringUid));
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    int numActivities = resultSet.getInt("count");
                    return new GetNumUserActivitiesByIdResult(user, commandRequest.getHeaders(), numActivities);
                } else {
                    return new WrongParametersResult();
                }
            } else {
                return new WrongParametersResult();
            }
        }
    }


    private String validatePathParameters(String uid) {
        String wrongParameters = "";
        int uidInt;
        try {
            uidInt = Integer.parseInt(uid);
        } catch (NumberFormatException | NullPointerException e) {
            return wrongParameters + "uid ";
        }
        if (uidInt < 1) {
            wrongParameters += "uid ";
        }
        return wrongParameters;
    }
}
