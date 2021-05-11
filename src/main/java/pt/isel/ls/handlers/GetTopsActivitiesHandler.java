package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.GetActivitiesResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.models.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class GetTopsActivitiesHandler implements CommandHandler {
    private static final int MAX_AMOUNT_OF_PARAMETERS = 4;
    private static final int MIN_AMOUNT_OF_PARAMETERS = 2;
    private static final int MID_AMOUNT_OF_PARAMETERS = 3;


    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            PreparedStatement pstmt;
            Parameters parameters = commandRequest.getParameters();
            String sid = parameters.get("sid");
            String orderBy = parameters.get("orderBy");
            String date = parameters.get("date");
            String rid = parameters.get("rid");

            String wrongParameters = checkParametersWithoutRid(sid, orderBy, date, conn);

            if (parameters.size() == MAX_AMOUNT_OF_PARAMETERS) {
                wrongParameters += checkRid(rid, conn);
                if (!wrongParameters.equals("")) {
                    conn.close();
                    return new WrongParametersResult(wrongParameters);
                }

                String sql = "SELECT * FROM activities WHERE sid=? AND date=? AND rid=? ORDER BY duration_time "
                        + orderBy;
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(3, Integer.parseInt(rid));
                pstmt.setDate(2, Date.valueOf(date));
                pstmt.setInt(1, Integer.parseInt(sid));
                ResultSet resultSet = pstmt.executeQuery();
                conn.close();
                return executeActivitiesResult(resultSet);

            } else if (parameters.size() == MID_AMOUNT_OF_PARAMETERS) {
                if (date != null) {
                    if (!wrongParameters.equals("")) {
                        conn.close();
                        return new WrongParametersResult(wrongParameters);
                    }

                    String sql = "SELECT * FROM activities WHERE sid=? AND date=? ORDER BY duration_time " + orderBy;
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setDate(2, Date.valueOf(date));
                    pstmt.setInt(1, Integer.parseInt(sid));
                    ResultSet resultSet = pstmt.executeQuery();
                    conn.close();
                    return executeActivitiesResult(resultSet);

                } else if (rid != null) {
                    wrongParameters += checkRid(rid, conn);
                    if (!wrongParameters.equals("")) {
                        conn.close();
                        return new WrongParametersResult(wrongParameters);
                    }

                    String sql = "SELECT * FROM activities WHERE sid=? AND rid=? ORDER BY duration_time " + orderBy;
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(2, Integer.parseInt(rid));
                    pstmt.setInt(1, Integer.parseInt(sid));
                    ResultSet resultSet = pstmt.executeQuery();
                    conn.close();
                    return executeActivitiesResult(resultSet);
                }
            } else if (parameters.size() == MIN_AMOUNT_OF_PARAMETERS) {
                if (!wrongParameters.equals("")) {
                    conn.close();
                    return new WrongParametersResult(wrongParameters);
                }

                String sql = "SELECT * FROM activities WHERE sid=? ORDER BY duration_time " + orderBy;
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(sid));
                ResultSet resultSet = pstmt.executeQuery();
                conn.close();
                return executeActivitiesResult(resultSet);
            }
            conn.close();
            return new WrongParametersResult(wrongParameters);

        } finally {
            conn.close();
        }
    }

    private CommandResult executeActivitiesResult(ResultSet resultSet) throws SQLException {
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
            return new GetActivitiesResult(activities);
        }
    }

    private String checkParametersWithoutRid(String sid, String orderBy, String date, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxSid = resultSet.getInt(1);

        String wrongParameters = "";
        if (sid == null || Integer.parseInt(sid) < 1 || maxSid < Integer.parseInt(sid)) {
            wrongParameters += " sid";
        }
        if (!orderBy.toLowerCase(Locale.ENGLISH).equals("asc")
                && !orderBy.toLowerCase(Locale.ENGLISH).equals("desc")) {
            wrongParameters += " order by";
        }
        if (date == null) {
            wrongParameters += " date";
        }
        return wrongParameters;
    }

    private String checkRid(String rid, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(rid) FROM routes";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxRid = resultSet.getInt(1);

        String wrongParameters = "";
        if (rid == null || Integer.parseInt(rid) < 1 || maxRid < Integer.parseInt(rid)) {
            wrongParameters += " rid";
        }

        return wrongParameters;
    }
}