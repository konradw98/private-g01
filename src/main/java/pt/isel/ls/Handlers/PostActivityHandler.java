package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;

import java.sql.*;
import java.util.Optional;

public class PostActivityHandler implements CommandHandler {
    private static final int MAX_AMOUNT_OF_PARAMETERS = 4;
    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        int uid = 0;
        Time duration = null;
        Date date = null;

        String sql;
        PreparedStatement pstmt;
        for (String param : commandRequest.getParameters()) {
            if (param.contains("uid")) uid = Integer.parseInt(param.substring(4).replace('+', ' '));
            else if (param.contains("duration")) duration = Time.valueOf(param.substring(9).replace('+', ' '));
            else if (param.contains("date")) date = Date.valueOf(param.substring(5).replace('+', ' '));
        }

        int paramSid = Integer.parseInt(commandRequest.getPathParameters().get(0));
        String wrongParameters = checkParametersWithoutRID(paramSid, uid, conn);

        if (commandRequest.getParameters().size() == MAX_AMOUNT_OF_PARAMETERS) {
            sql = "INSERT INTO activities(date,duration_time,sid,uid,rid) values(?,?,?,?,?)";
            int rid = 0;
            for (String param : commandRequest.getParameters()) {
                if (param.contains("rid")) rid = Integer.parseInt(param.substring(4));
            }
            wrongParameters += checkRID(rid, conn);

            if (!wrongParameters.equals("")) {
                conn.close();
                System.out.println("Wrong parameters:" + wrongParameters);
                return Optional.empty();
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(5, rid);
        } else if (!wrongParameters.equals("")) {
            conn.close();
            System.out.println("Wrong parameters:" + wrongParameters);
            return Optional.empty();
        } else if (commandRequest.getParameters().size() < MAX_AMOUNT_OF_PARAMETERS) {
            sql = "INSERT INTO activities(date,duration_time,sid,uid) values(?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
        } else return Optional.empty();

        pstmt.setDate(1, date);
        pstmt.setTime(2, duration);
        pstmt.setInt(3, paramSid);
        pstmt.setInt(4, uid);
        pstmt.executeUpdate();

        String sql1 = "SELECT MAX(aid) FROM activities";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt1.executeQuery()));
        conn.close();
        return optional;
    }

    private String checkParametersWithoutRID(int sid, int uid, Connection conn) throws SQLException {
        String sql = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        int maxSID = resultSet.getInt(1);

        sql = "SELECT MAX(uid) FROM users";
        pstmt = conn.prepareStatement(sql);
        resultSet = pstmt.executeQuery();
        resultSet.next();
        int maxUID = resultSet.getInt(1);

        String wrongParameters = "";
        if (uid < 1 || uid > maxUID) {
            wrongParameters += " uid = " + uid;
        }
        if (sid < 1 || sid > maxSID) {
            wrongParameters += " sid = " + sid;
        }

        return wrongParameters;
    }

    private String checkRID(int rid, Connection conn) throws SQLException {
        String sqlCheck = "SELECT MAX(rid) FROM routes";
        PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
        ResultSet resultSet = pstmtCheck.executeQuery();
        resultSet.next();
        int maxRID = resultSet.getInt(1);

        String wrongParameters = "";
        if (rid < 1 || rid > maxRID) {
            wrongParameters += " rid = " + rid;
        }

        return wrongParameters;
    }
}
