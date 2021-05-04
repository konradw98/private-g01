package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;
import pt.isel.ls.CommandResults.GetActivitiesResult;
import pt.isel.ls.CommandResults.GetUsersResult;
import pt.isel.ls.CommandResults.WrongParametersResult;
import pt.isel.ls.Models.Activity;
import pt.isel.ls.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class GetSportActivitiesHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            ArrayList<String> parameters = commandRequest.getPathParameters();

            String sql = "SELECT * FROM activities WHERE sid=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(parameters.get(0)));
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
        } finally {
            conn.close();
        }
    }
}
