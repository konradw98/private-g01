package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
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
            int uid = 0;
            Time duration = null;
            Date date = null;

            String sql;
            PreparedStatement pstmt;
            for (String param : commandRequest.getParameters()) {
                if (param.contains("uid")) {
                    uid = Integer.parseInt(param.substring(4).replace('+', ' '));
                } else if (param.contains("duration")) {
                    duration = Time.valueOf(param.substring(9).replace('+', ' '));
                } else if (param.contains("date")) {
                    date = Date.valueOf(param.substring(5).replace('+', ' '));
                }
            }

            int paramSid = Integer.parseInt(commandRequest.getPathParameters().get("sid"));
            String wrongParameters = checkParametersWithoutRid(paramSid, uid, conn);

            if (commandRequest.getParameters().size() == MAX_AMOUNT_OF_PARAMETERS) {
                sql = "INSERT INTO activities(date,duration_time,sid,uid,rid) values(?,?,?,?,?)";
                int rid = 0;
                for (String param : commandRequest.getParameters()) {
                    if (param.contains("rid")) {
                        rid = Integer.parseInt(param.substring(4));
                    }
                }
                wrongParameters += checkRid(rid, conn);

                if (!wrongParameters.equals("")) {
                    conn.close();
                    return new WrongParametersResult();
                }

                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(5, rid);

            } else if (!wrongParameters.equals("")) {
                conn.close();
                return new WrongParametersResult();

            } else if (commandRequest.getParameters().size() < MAX_AMOUNT_OF_PARAMETERS) {

                sql = "INSERT INTO activities(date,duration_time,sid,uid) values(?,?,?,?)";
                pstmt = conn.prepareStatement(sql);

            } else {
                return new WrongParametersResult();
            }

            pstmt.setDate(1, date);
            pstmt.setTime(2, duration);
            pstmt.setInt(3, paramSid);
            pstmt.setInt(4, uid);
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

    private String checkParametersWithoutRid(int sid, int uid, Connection conn) throws SQLException {
        String sql = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        int maxSid = resultSet.getInt(1);

        String wrongParameters = "";
        if (sid < 1 || sid > maxSid) {
            wrongParameters += " sid = " + sid;
        }

        sql = "SELECT MAX(uid) FROM users";
        pstmt = conn.prepareStatement(sql);
        resultSet = pstmt.executeQuery();
        resultSet.next();
        int maxUid = resultSet.getInt(1);

        if (uid < 1 || uid > maxUid) {
            wrongParameters += " uid = " + uid;
        }

        return wrongParameters;
    }

    private String checkRid(int rid, Connection conn) throws SQLException {
        String sqlCheck = "SELECT MAX(rid) FROM routes";
        PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
        ResultSet resultSet = pstmtCheck.executeQuery();
        resultSet.next();
        int maxRid = resultSet.getInt(1);

        String wrongParameters = "";
        if (rid < 1 || rid > maxRid) {
            wrongParameters += " rid = " + rid;
        }

        return wrongParameters;
    }
}
