package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.PathParameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.getresult.GetActivitiesResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.models.Activity;
import java.sql.*;
import java.util.ArrayList;

public class GetSportActivitiesByIdHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PathParameters pathParameters = commandRequest.getPathParameters();
        String stringSid = pathParameters.get("sid");
        String stringAid = pathParameters.get("aid");
        String wrongParameters = validatePathParameters(stringSid, stringAid);
        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            String sql = "SELECT * FROM activities WHERE sid=? AND aid=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(stringSid));
            pstmt.setInt(2, Integer.parseInt(stringAid));
            ResultSet resultSet = pstmt.executeQuery();
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
                return new GetActivitiesResult(activities);
            }
        } finally {
            conn.close();
        }
    }

    private String validatePathParameters(String sid, String aid) {
        String wrongParameters = "";
        if (sid == null || Integer.parseInt(sid) < 1) {
            wrongParameters += "sid ";
        }

        if (aid == null || Integer.parseInt(aid) < 1) {
            wrongParameters += "aid ";
        }
        return wrongParameters;
    }
}
