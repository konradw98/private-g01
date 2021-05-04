package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;
import pt.isel.ls.CommandResults.GetActivitiesResult;
import pt.isel.ls.CommandResults.WrongParametersResult;
import pt.isel.ls.Models.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

public class GetTopsActivitiesHandler implements CommandHandler {
    private static final int MAX_AMOUNT_OF_PARAMETERS = 4;
    private static final int MIN_AMOUNT_OF_PARAMETERS = 2;
    private static final int MID_AMOUNT_OF_PARAMETERS = 3;


    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        int sid = 0;
        String orderBy = "";
        Date date = null;
        int rid = 0;
        PreparedStatement pstmt;
        ArrayList<String> parameters = commandRequest.getParameters();

        for (String param : parameters) {
            if (param.contains("sid")) sid = Integer.parseInt(param.substring(4));
            else if (param.contains("orderBy")) orderBy = param.substring(8).replace('+', ' ');
            else if (param.contains("date")) date = Date.valueOf(param.substring(5));
            else if (param.contains("rid")) rid = Integer.parseInt(param.substring(4));
        }

        String wrongParameters = checkParametersWithoutRID(sid, orderBy, conn);

        if (parameters.size() == MAX_AMOUNT_OF_PARAMETERS) {
            wrongParameters += checkRID(rid, conn);
            if (!wrongParameters.equals("")) {
                conn.close();
                return new WrongParametersResult(wrongParameters);
            }

            String sql = "SELECT * FROM activities WHERE sid=? AND date=? AND rid=? ORDER BY duration_time " + orderBy;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(3, rid);
            pstmt.setDate(2, date);
            pstmt.setInt(1, sid);
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
                pstmt.setDate(2, date);
                pstmt.setInt(1, sid);
                ResultSet resultSet = pstmt.executeQuery();
                conn.close();
                return executeActivitiesResult(resultSet);

            } else if (rid != 0) {
                wrongParameters += checkRID(rid, conn);
                if (!wrongParameters.equals("")) {
                    conn.close();
                    return new WrongParametersResult(wrongParameters);
                }

                String sql = "SELECT * FROM activities WHERE sid=? AND rid=? ORDER BY duration_time " + orderBy;
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(2, rid);
                pstmt.setInt(1, sid);
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
            pstmt.setInt(1, sid);
            pstmt.setInt(1, sid);
            ResultSet resultSet = pstmt.executeQuery();
            conn.close();
            return executeActivitiesResult(resultSet);
        }
        conn.close();
        return new WrongParametersResult(wrongParameters);
    }

    private CommandResult executeActivitiesResult(ResultSet resultSet) throws SQLException {
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

    private String checkParametersWithoutRID(int sid, String orderBy, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxSID = resultSet.getInt(1);

        String wrongParameters = "";
        if (sid < 1 || maxSID < sid) {
            wrongParameters += " sid = " + sid;
        }
        if (!orderBy.toLowerCase(Locale.ENGLISH).equals("asc") && !orderBy.toLowerCase(Locale.ENGLISH).equals("desc")) {
            wrongParameters += " order by = " + orderBy;
        }
        return wrongParameters;
    }

    private String checkRID(int rid, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(rid) FROM routes";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxRID = resultSet.getInt(1);

        String wrongParameters = "";
        if (rid < 1 || maxRID < rid) {
            wrongParameters += " rid = " + rid;
        }

        return wrongParameters;
    }
}