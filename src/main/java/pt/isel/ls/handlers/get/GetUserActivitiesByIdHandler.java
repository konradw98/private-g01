package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.getresult.GetActivitiesResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Activity;
import java.sql.*;
import java.util.ArrayList;

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

        Connection conn = commandRequest.getDataSource().getConnection();
        try {
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

            String sql1 = "SELECT * FROM activities WHERE uid=? AND aid=?";
            pstmt = conn.prepareStatement(sql1);
            pstmt.setInt(1, Integer.parseInt(stringUid));
            pstmt.setInt(2, Integer.parseInt(stringAid));
            resultSet = pstmt.executeQuery();
            conn.close();

            int aid;
            int sid;
            int rid;
            int uid;
            Date date;
            Time durationTime;
            Activity activity;
            ArrayList<Activity> activities = new ArrayList<>();

            while (resultSet.next()) {
                aid = resultSet.getInt("aid");
                date = resultSet.getDate("date");
                durationTime = resultSet.getTime("duration_time");
                sid = resultSet.getInt("sid");
                uid = resultSet.getInt("uid");
                rid = resultSet.getInt("rid");
                activity = new Activity(aid, date, durationTime, sid, uid, rid);
                activities.add(activity);
            }
            if (activities.size() == 0) {
                return new WrongParametersResult(wrongParameters);
            } else {
                return new GetActivitiesResult(activities, commandRequest.getHeaders());
            }
        } finally {
            conn.close();
        }
    }

    private String validatePathParameters(String uid, String aid) {
        String wrongParameters = "";
        int uidInt;
        try {
            uidInt = Integer.parseInt(uid);
        } catch (NumberFormatException e) {
            return wrongParameters + "uid ";
        }
        if (uid == null || uidInt < 1) {
            wrongParameters += "uid ";
        }

        int aidInt;
        try {
            aidInt = Integer.parseInt(aid);
        } catch (NumberFormatException e) {
            return wrongParameters + "aid ";
        }
        if (aid == null || aidInt < 1) {
            wrongParameters += "aid ";
        }
        return wrongParameters;
    }

}
