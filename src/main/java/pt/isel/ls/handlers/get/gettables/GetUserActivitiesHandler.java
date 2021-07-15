package pt.isel.ls.handlers.get.gettables;

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

public class GetUserActivitiesHandler extends GetTablesHandler implements CommandHandler {
    private ArrayList<Activity> activities;

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {

        if (!commandRequest.hasPagingParameters()) {
            return new WrongParametersResult("skip and top missing");
        }

        Parameters parameters = commandRequest.getParameters();
        String skip = parameters.get("skip");
        String top = parameters.get("top");

        String wrongParameters = validateParameters(skip, top);

        String stringUid = commandRequest.getPathParameters().get("uid");
        wrongParameters += validatePathParameters(stringUid);

        Headers headers = commandRequest.getHeaders();
        wrongParameters += validateHeaders(headers);

        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        int skipInt = Integer.parseInt(skip);
        try (Connection conn = commandRequest.getDataSource().getConnection()) {
            String sql = "SELECT COUNT(*) FROM activities WHERE uid=? AND timestamp IS NULL";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(stringUid));
            ResultSet resultSet = pstmt.executeQuery();
            int count = 1;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count == 0) {
                return new EmptyTableResult("activities");
            }

            String sql2 = "SELECT * FROM activities WHERE uid=? AND timestamp IS NULL";
            pstmt = conn.prepareStatement(sql2);
            pstmt.setInt(1, Integer.parseInt(stringUid));
            resultSet = pstmt.executeQuery();

            int aid;
            int sid;
            int rid;
            int uid;
            Date date;
            Time durationTime;
            Activity activity;
            activities = new ArrayList<>();

            int i = 0;
            while (resultSet.next()) {
                if (i >= skipInt && i < skipInt + Integer.parseInt(top)) {
                    aid = resultSet.getInt("aid");
                    date = resultSet.getDate("date");
                    durationTime = resultSet.getTime("duration_time");
                    sid = resultSet.getInt("sid");
                    uid = resultSet.getInt("uid");
                    rid = resultSet.getInt("rid");
                    activity = new Activity(aid, date, durationTime, sid, uid, rid);
                    activities.add(activity);
                }
                i++;
            }
            if (activities.size() == 0) {
                return new WrongParametersResult();
            } else {
                return new GetActivitiesResult(activities, count, commandRequest.getHeaders(),
                        commandRequest.getParameters());
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

    public ArrayList<Activity> getActivities() {
        return activities;
    }
}
