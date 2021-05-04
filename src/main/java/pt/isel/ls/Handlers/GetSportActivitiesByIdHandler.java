package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;
import pt.isel.ls.CommandResults.GetActivitiesResult;
import pt.isel.ls.CommandResults.WrongParametersResult;
import pt.isel.ls.Models.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class GetSportActivitiesByIdHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        ArrayList<String> parameters = commandRequest.getPathParameters();
        int sidParam = Integer.parseInt(parameters.get(0));
        int aidParam = Integer.parseInt(parameters.get(1));

        String wrongParameters = checkParameters(sidParam, aidParam, conn);
        if (!wrongParameters.equals("")) {
            conn.close();
            System.out.println("Wrong parameter:" + wrongParameters);
            return new WrongParametersResult();
        }

        String sql = "SELECT * FROM activities WHERE sid=? AND aid=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, sidParam);
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
            return new WrongParametersResult();
        } else return new GetActivitiesResult(activities);
    }

    private String checkParameters(int sid, int aid, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxSID = resultSet.getInt(1);

        sql1 = "SELECT MAX(aid) FROM activities";
        pstmt1 = conn.prepareStatement(sql1);
        resultSet = pstmt1.executeQuery();
        resultSet.next();

        String wrongParameters = "";
        if (resultSet.getInt(1) < aid) {
            wrongParameters += " aid = " + aid;
        }

        if (maxSID < sid || sid < 1) {
            wrongParameters += " sid = " + sid;
        }

        return wrongParameters;
    }
}
