package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.ResourcesNotFoundResult;
import pt.isel.ls.commandresults.getresult.GetUserByIdResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Activity;
import pt.isel.ls.models.Sport;
import pt.isel.ls.models.User;
import java.sql.*;
import java.util.ArrayList;

public class GetUserByIdHandler extends GetHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws Exception {
        String stringUid = commandRequest.getPathParameters().get("uid");
        String wrongParameters = validatePathParameters(stringUid);

        Parameters parameters = commandRequest.getParameters();
        wrongParameters += validateParameters(parameters);

        Headers headers = commandRequest.getHeaders();
        wrongParameters += validateHeaders(headers);

        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        try (Connection conn = commandRequest.getDataSource().getConnection()) {
            String sql = "SELECT COUNT(*) FROM users";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            int count = 1;
            int uid = Integer.parseInt(stringUid);
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count == 0) {
                return new EmptyTableResult("users");
            } else if (count < uid) {
                return new WrongParametersResult("user with uid=" + uid + "not found");
            }

            String sql1 = "SELECT * FROM activities WHERE uid=" + uid + " AND timestamp IS NULL";
            pstmt = conn.prepareStatement(sql1);
            resultSet = pstmt.executeQuery();

            int aid;
            Date date;
            Time durationTime;
            int activitySid;
            int activityUid;
            int rid;
            Activity activity;
            ArrayList<Activity> activities = new ArrayList<>();

            while (resultSet.next()) {
                aid = resultSet.getInt("aid");
                date = resultSet.getDate("date");
                durationTime = resultSet.getTime("duration_time");
                activitySid = resultSet.getInt("sid");
                activityUid = resultSet.getInt("uid");
                rid = resultSet.getInt("rid");
                activity = new Activity(aid, date, durationTime, activitySid, activityUid, rid);
                activities.add(activity);
            }

            String sql2 = "SELECT * FROM sports WHERE sid IN (SELECT sid FROM activities WHERE uid = " + uid + ")";
            pstmt = conn.prepareStatement(sql2);
            resultSet = pstmt.executeQuery();

            int sid;
            String sportName;
            String description;
            Sport sport;
            ArrayList<Sport> sports = new ArrayList<>();

            while (resultSet.next()) {
                sid = resultSet.getInt("sid");
                sportName = resultSet.getString("name");
                description = resultSet.getString("description");
                sport = new Sport(sid, sportName, description);
                sports.add(sport);

            }

            String sql3 = "SELECT * FROM users WHERE uid=?";
            pstmt = conn.prepareStatement(sql3);
            pstmt.setInt(1, Integer.parseInt(stringUid));
            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                uid = resultSet.getInt("uid");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                User user = new User(uid, email, name);
                return new GetUserByIdResult(user, commandRequest.getHeaders(), sports, activities);
            } else {
                return new ResourcesNotFoundResult();
            }
        }
    }

    private String validateParameters(Parameters parameters) {
        if (parameters != null) {
            return "no parameters are needed ";
        } else {
            return "";
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
