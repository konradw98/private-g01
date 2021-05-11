package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.GetActivitiesResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.models.Activity;
import java.sql.*;
import java.util.ArrayList;

public class GetSportActivitiesByIdHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            ArrayList<String> parameters = commandRequest.getPathParameters();
            int sidParam = Integer.parseInt(parameters.get(0));
            int aidParam = Integer.parseInt(parameters.get(1));

            String wrongParameters = checkParameters(sidParam, aidParam, conn);
            if (!wrongParameters.equals("")) {
                conn.close();
                return new WrongParametersResult(wrongParameters);
            }

            String sql = "SELECT * FROM activities WHERE sid=? AND aid=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sidParam);
            pstmt.setInt(2, aidParam);
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

    private String checkParameters(int sid, int aid, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxSid = resultSet.getInt(1);

        String wrongParameters = "";
        if (maxSid < sid || sid < 1) {
            wrongParameters += " sid = " + sid;
        }

        sql1 = "SELECT MAX(aid) FROM activities";
        pstmt1 = conn.prepareStatement(sql1);
        resultSet = pstmt1.executeQuery();
        resultSet.next();

        if (resultSet.getInt(1) < aid) {
            wrongParameters += " aid = " + aid;
        }

        return wrongParameters;
    }
}