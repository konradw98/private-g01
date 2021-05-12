package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.PostResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import java.sql.*;

public class PostActivityHandler implements CommandHandler {
    private static final int MAX_AMOUNT_OF_PARAMETERS = 4;

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();

        try {
            String sql;
            PreparedStatement pstmt;
            Parameters parameters = commandRequest.getParameters();
            String uid = parameters.get("uid");
            String duration = parameters.get("duration");
            String date = parameters.get("date");
            String paramSid = commandRequest.getPathParameters().get("sid");

            String wrongParameters = checkParametersWithoutRid(paramSid, uid, duration, date, conn);

            if (commandRequest.getParameters().size() == MAX_AMOUNT_OF_PARAMETERS) {
                sql = "INSERT INTO activities(date,duration_time,sid,uid,rid) values(?,?,?,?,?)";
                String rid = parameters.get("rid");
                wrongParameters += checkRid(rid, conn);

                if (!wrongParameters.equals("")) {
                    conn.close();
                    return new WrongParametersResult();
                }

                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(5, Integer.parseInt(rid));

            } else if (!wrongParameters.equals("")) {
                conn.close();
                return new WrongParametersResult();

            } else if (commandRequest.getParameters().size() < MAX_AMOUNT_OF_PARAMETERS) {

                sql = "INSERT INTO activities(date,duration_time,sid,uid) values(?,?,?,?)";
                pstmt = conn.prepareStatement(sql);

            } else {
                return new WrongParametersResult();
            }

            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setTime(2, Time.valueOf(duration));
            pstmt.setInt(3, Integer.parseInt(paramSid));
            pstmt.setInt(4, Integer.parseInt(uid));
            pstmt.executeUpdate();

            String sql1 = "SELECT MAX(aid) FROM activities";
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            ResultSet resultSet = pstmt1.executeQuery();
            conn.close();

            if (resultSet.next()) {
                int aid = resultSet.getInt("max");
                return new PostResult(aid, "aid");
            } else {
                return new WrongParametersResult();
            }

        } finally {
            conn.close();
        }
    }

    private String checkParametersWithoutRid(String sid, String uid, String duration, String date, Connection conn)
            throws SQLException {
        String sql = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        int maxSid = resultSet.getInt(1);

        String wrongParameters = "";
        if (sid == null || Integer.parseInt(sid) < 1 || Integer.parseInt(sid) > maxSid) {
            wrongParameters += " sid";
        }

        sql = "SELECT MAX(uid) FROM users";
        pstmt = conn.prepareStatement(sql);
        resultSet = pstmt.executeQuery();
        resultSet.next();
        int maxUid = resultSet.getInt(1);

        if (uid == null || Integer.parseInt(uid) < 1 || Integer.parseInt(uid) > maxUid) {
            wrongParameters += " uid";
        }

        if (duration == null) {
            wrongParameters += " duration";
        }

        if (date == null) {
            wrongParameters += " date";
        }
        return wrongParameters;
    }

    private String checkRid(String rid, Connection conn) throws SQLException {
        String sqlCheck = "SELECT MAX(rid) FROM routes";
        PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
        ResultSet resultSet = pstmtCheck.executeQuery();
        resultSet.next();
        int maxRid = resultSet.getInt(1);

        String wrongParameters = "";
        if (rid == null || Integer.parseInt(rid) < 1 || Integer.parseInt(rid) > maxRid) {
            wrongParameters += " rid";
        }

        return wrongParameters;
    }
}
