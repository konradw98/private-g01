package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.getresult.GetActivitiesResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Activity;
import java.sql.*;
import java.util.ArrayList;

public class GetUserActivitiesHandler extends GetHandler implements CommandHandler {
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
            String sql = "SELECT COUNT(*) FROM activites";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            int count = 1;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count == 0) {
                return new EmptyTableResult("activities");
            }

            String sql1 = "SELECT * FROM activities WHERE uid=?";
            pstmt = conn.prepareStatement(sql1);
            pstmt.setInt(1, Integer.parseInt(stringUid));
            resultSet = pstmt.executeQuery();

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
                return new WrongParametersResult();
            } else {
                return new GetActivitiesResult(activities, commandRequest.getHeaders());
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
