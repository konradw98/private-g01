package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.ResourcesNotFoundResult;
import pt.isel.ls.commandresults.getresult.GetRouteResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Activity;
import pt.isel.ls.models.Route;
import pt.isel.ls.models.Sport;
import java.sql.*;
import java.util.ArrayList;

public class GetRouteByIdHandler extends GetHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        String stringRid = commandRequest.getPathParameters().get("rid");
        String wrongParameters = validatePathParameters(stringRid);

        Headers headers = commandRequest.getHeaders();
        wrongParameters += validateHeaders(headers);

        Parameters parameters = commandRequest.getParameters();
        wrongParameters += validateParameters(parameters);

        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }
        int rid = Integer.parseInt(stringRid);

        try (Connection conn = commandRequest.getDataSource().getConnection()) {
            String sql = "SELECT COUNT(*) FROM routes";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            int count = 1;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count == 0) {
                return new EmptyTableResult("routes");
            }

            String sql1 = "SELECT * FROM activities WHERE rid=" + rid + " AND timestamp IS NULL";
            pstmt = conn.prepareStatement(sql1);
            resultSet = pstmt.executeQuery();

            int aid;
            Date date;
            Time durationTime;
            int activitySid;
            int activityUid;
            int activityRid;
            Activity activity;
            ArrayList<Activity> activities = new ArrayList<>();

            while (resultSet.next()) {
                aid = resultSet.getInt("aid");
                date = resultSet.getDate("date");
                durationTime = resultSet.getTime("duration_time");
                activitySid = resultSet.getInt("sid");
                activityUid = resultSet.getInt("uid");
                activityRid = resultSet.getInt("rid");
                activity = new Activity(aid, date, durationTime, activitySid, activityUid, activityRid);
                activities.add(activity);
            }

            String sql2 = "SELECT * FROM sports WHERE sid IN (SELECT sid FROM activities WHERE rid = " + rid + ")";
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

            String sql3 = "SELECT * FROM routes WHERE rid=?";
            pstmt = conn.prepareStatement(sql3);
            pstmt.setInt(1, Integer.parseInt(stringRid));
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                rid = resultSet.getInt("rid");
                String startLocation = resultSet.getString("start_location");
                String endLocation = resultSet.getString("end_location");
                double distance = resultSet.getDouble("distance");
                Route route = new Route(rid, startLocation, endLocation, distance);
                return new GetRouteResult(route, commandRequest.getHeaders(), sports, activities);
            } else {
                return new ResourcesNotFoundResult();
            }
        }
    }

    private String validatePathParameters(String rid) {
        String wrongParameters = "";
        int ridInt;
        try {
            ridInt = Integer.parseInt(rid);
        } catch (NumberFormatException | NullPointerException e) {
            return wrongParameters + "rid ";
        }
        if (ridInt < 1) {
            wrongParameters += "rid ";
        }
        return wrongParameters;
    }

    private String validateParameters(Parameters parameters) {
        if (parameters != null) {
            return "no parameters are needed ";
        } else {
            return "";
        }
    }
}
