package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;
import pt.isel.ls.CommandResults.GetActivitiesResult;
import pt.isel.ls.CommandResults.WrongParametersResult;
import pt.isel.ls.Models.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class GetUserActivitiesByIdHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            ArrayList<String> parameters = commandRequest.getParameters();

            int uidParam = Integer.parseInt(parameters.get(0));
            int aidParam = Integer.parseInt(parameters.get(1));
            String wrongParameters = checkParameters(uidParam, aidParam, conn);

            if (!wrongParameters.equals("")) {
                conn.close();
                return new WrongParametersResult(wrongParameters);
            }

            String sql = "SELECT * FROM activities WHERE uid=? AND aid=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, uidParam);
            pstmt.setInt(2, aidParam);
            ResultSet resultSet = pstmt.executeQuery();
            conn.close();

            int aid, sid, rid, uid;
            Date date;
            Time duration_time;
            Activity activity;
            ArrayList<Activity> activities = new ArrayList<>();

            while (resultSet.next()) {
                aid = resultSet.getInt("aid");
                date = resultSet.getDate("date");
                duration_time = resultSet.getTime("duration_time");
                sid = resultSet.getInt("sid");
                uid = resultSet.getInt("uid");
                rid = resultSet.getInt("rid");
                activity = new Activity(aid, date, duration_time, sid, uid, rid);
                activities.add(activity);
            }
            if (activities.size() == 0) {
                return new WrongParametersResult(wrongParameters);
            } else return new GetActivitiesResult(activities);
        }finally {
            conn.close();
        }
    }

    private String checkParameters(int uid, int aid, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(uid) FROM users";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxUID = resultSet.getInt(1);

        sql1 = "SELECT MAX(aid) FROM activities";
        pstmt1 = conn.prepareStatement(sql1);
        resultSet = pstmt1.executeQuery();
        resultSet.next();

        String wrongParameters = "";
        if (resultSet.getInt(1) < aid || aid < 1) {
            wrongParameters += " aid = " + aid;
        }

        if (maxUID < uid || uid < 1) {
            wrongParameters += " uid = " + uid;
        }
        return wrongParameters;
    }

}
