package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
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
            int sid = 0;
            String orderBy = "";
            Date date = null;
            int rid = 0;
            PreparedStatement pstmt;
            ArrayList<String> parameters = commandRequest.getParameters();

            for (String param : parameters) {
                if (param.contains("sid")) {
                    sid = Integer.parseInt(param.substring(4));
                } else if (param.contains("orderBy")) {
                    orderBy = param.substring(8);
                } else if (param.contains("date")) {
                    date = Date.valueOf(param.substring(5));
                } else if (param.contains("rid")) {
                    rid = Integer.parseInt(param.substring(4));
                }
            }

            String wrongParameters = checkParametersWithoutRid(sid, orderBy, conn);

            if (parameters.size() == MAX_AMOUNT_OF_PARAMETERS) {
                wrongParameters += checkRid(rid, conn);
                if (!wrongParameters.equals("")) {
                    conn.close();
                    return new WrongParametersResult(wrongParameters);
                }

                String sql = "SELECT * FROM activities WHERE sid=? AND date=? AND rid=? ORDER BY duration_time "
                        + orderBy;
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
                    wrongParameters += checkRid(rid, conn);
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

    private String checkParametersWithoutRid(int sid, String orderBy, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxSid = resultSet.getInt(1);

        String wrongParameters = "";
        if (sid < 1 || maxSid < sid) {
            wrongParameters += " sid = " + sid;
        }
        if (!orderBy.toLowerCase(Locale.ENGLISH).equals("asc") && !orderBy.toLowerCase(Locale.ENGLISH).equals("desc")) {
            wrongParameters += " order by = " + orderBy;
        }
        return wrongParameters;
    }

    private String checkRid(int rid, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(rid) FROM routes";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxRid = resultSet.getInt(1);

        String wrongParameters = "";
        if (rid < 1 || maxRid < rid) {
            wrongParameters += " rid = " + rid;
        }

        return wrongParameters;
    }
}