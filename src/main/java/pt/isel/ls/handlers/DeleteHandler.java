package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.DeleteResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class DeleteHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        List<String> activities = new ArrayList<>();
        for (int i = 0; i < commandRequest.getParameters().size(); i++) {
            activities.add(commandRequest.getParameters().get("activity" + i));
        }

        Connection conn = commandRequest.getDataSource().getConnection();
        String stringUid = commandRequest.getPathParameters().get("uid");
        String wrongParameters = validatePathParameters(stringUid);
        wrongParameters += validateParameters(activities, conn);
        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        conn.setAutoCommit(false);
        Savepoint savepoint1 = conn.setSavepoint("Savepoint1");
        int uid = Integer.parseInt(stringUid);
        try {
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());

            boolean bug=false;
            int result=1;
            for (String id : activities) {
                String sql = "UPDATE activities SET timestamp=? WHERE uid=? AND aid=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setTimestamp(1, ts);
                pstmt.setInt(2, uid);
                pstmt.setInt(3, Integer.parseInt(id));
                result=pstmt.executeUpdate();
                if(result<1) bug=true;
                conn.commit();
            }

            if(bug) conn.rollback(savepoint1);

        } catch (SQLException sql) {
            conn.rollback(savepoint1);
            return new WrongParametersResult();
        } finally {
            conn.close();
        }

        return new DeleteResult(uid, activities);
    }

    private String validatePathParameters(String uid) {
        String wrongParameters = "";
        if (uid == null || Integer.parseInt(uid) < 1) {
            wrongParameters += "uid ";
        }
        return wrongParameters;
    }

    private String validateParameters(List<String> activities, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(aid) FROM activities";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        conn.close();

        int maxAid = Integer.MAX_VALUE;
        if (resultSet.next()) {
            maxAid = resultSet.getInt("max");
        }

        String wrongParameters = "";
        for (String id : activities) {
            if (id == null || Integer.parseInt(id) < 1 || Integer.parseInt(id) > maxAid) {
                wrongParameters += "activity = " + id + " ";
            }
        }
        return wrongParameters;
    }
}
