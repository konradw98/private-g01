package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.commandresults.getresult.GetActivityResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Activity;
import java.sql.*;

public class GetUserActivitiesByIdHandler extends GetHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Parameters pathParameters = commandRequest.getParameters();
        String stringUid = pathParameters.get("uid");
        String stringAid = pathParameters.get("aid");
        String wrongParameters = validatePathParameters(stringUid, stringAid);

        Headers headers = commandRequest.getHeaders();
        wrongParameters += validateHeaders(headers);

        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        try (Connection conn = commandRequest.getDataSource().getConnection()) {
            String sql = "SELECT COUNT(*) FROM activities";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            int count = 1;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count == 0) {
                return new EmptyTableResult("activites");
            }

            String sql1 = "SELECT * FROM activities WHERE uid=? AND aid=? AND timestamp IS NULL";
            pstmt = conn.prepareStatement(sql1);
            pstmt.setInt(1, Integer.parseInt(stringUid));
            pstmt.setInt(2, Integer.parseInt(stringAid));
            resultSet = pstmt.executeQuery();

            int aid;
            int sid;
            int rid;
            int uid;
            Date date;
            Time durationTime;
            Activity activity;

            if (resultSet.next()) {
                aid = resultSet.getInt("aid");
                date = resultSet.getDate("date");
                durationTime = resultSet.getTime("duration_time");
                sid = resultSet.getInt("sid");
                uid = resultSet.getInt("uid");
                rid = resultSet.getInt("rid");
                activity = new Activity(aid, date, durationTime, sid, uid, rid);
                return new GetActivityResult(activity, commandRequest.getHeaders());
            }
            else {
                return new WrongParametersResult(wrongParameters);
            }
        }
    }

    private String validatePathParameters(String uid, String aid) {
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

        int aidInt;
        try {
            aidInt = Integer.parseInt(aid);
        } catch (NumberFormatException | NullPointerException e) {
            return wrongParameters + "aid ";
        }
        if (aidInt < 1) {
            wrongParameters += "aid ";
        }
        return wrongParameters;
    }

}
