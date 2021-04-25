package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.*;
import java.util.Optional;

public class PostActivityHandler implements CommandHandler {
    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {

        Connection conn = commandRequest.getDataSource().getConnection();

        int paramUid = 0;
        int paramTime = -1;
        Date paramDate = null;

        //checking the max uid and rid
        String sqlCheck = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
        ResultSet resultSet = pstmtCheck.executeQuery();
        resultSet.next();
        int maxSID = resultSet.getInt(1);

        sqlCheck = "SELECT MAX(uid) FROM users";
        pstmtCheck = conn.prepareStatement(sqlCheck);
        resultSet = pstmtCheck.executeQuery();
        resultSet.next();
        int maxUID = resultSet.getInt(1);

        String sql;
        PreparedStatement pstmt;
        for (String param : commandRequest.getParameters()) {
            if (param.contains("uid")) paramUid = Integer.parseInt(param.substring(4).replace('+', ' '));
            else if (param.contains("time")) paramTime = Integer.parseInt(param.substring(9).replace('+', ' '));
            else if (param.contains("date")) paramDate = Date.valueOf(param.substring(5).replace('+', ' '));
        }

        int paramSid = Integer.parseInt(commandRequest.getPathParameters().get(0));

        String wrongParameterName = "";
        boolean ifWrongParameters = false;
        if (paramUid < 1 || paramUid > maxUID) {
            wrongParameterName += " uid = " + paramUid;
            ifWrongParameters = true;
        }
        if (paramSid < 1 || paramSid > maxSID) {
            wrongParameterName += " sid = " + paramSid;
            ifWrongParameters = true;
        }

        if (commandRequest.getParameters().size() == 4) {
            sql = "INSERT INTO activities(date,duration_time,sid,uid,rid) values(?,?,?,?,?)";
            int paramRid = 0;
            for (String param : commandRequest.getParameters()) {
                if (param.contains("rid")) paramRid = Integer.parseInt(param.substring(4));
            }
            sqlCheck = "SELECT MAX(rid) FROM routes";
            pstmtCheck = conn.prepareStatement(sqlCheck);
            resultSet = pstmtCheck.executeQuery();
            resultSet.next();
            int maxRID = resultSet.getInt(1);

            if (paramRid < 1 || paramRid > maxRID) {
                conn.close();
                wrongParameterName += " rid = " + paramRid;
                System.out.println("Wrong parameter:" + wrongParameterName);
                return Optional.empty();
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(5, paramRid);
        } else if (ifWrongParameters) {
            conn.close();
            System.out.println("Wrong parameter:" + wrongParameterName);
            return Optional.empty();
        } else if (commandRequest.getParameters().size() < 4) {
            sql = "INSERT INTO activities(date,duration_time,sid,uid) values(?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
        } else return Optional.empty();

        pstmt.setDate(1, paramDate);
        pstmt.setInt(2, paramTime);
        pstmt.setInt(3, paramSid);
        pstmt.setInt(4, paramUid);
        pstmt.executeUpdate();

        String sql1 = "SELECT MAX(aid) FROM activities";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt1.executeQuery()));
        conn.close();
        return optional;
    }
}
