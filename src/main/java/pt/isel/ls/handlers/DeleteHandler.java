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
        wrongParameters += validateParameters(activities);
        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        conn.setAutoCommit(false);
        Savepoint savepoint1 = conn.setSavepoint("Savepoint1");
        int uid = Integer.parseInt(stringUid);
        try {
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());

            boolean bug = false;
            int result;
            for (String id : activities) {
                String sql = "UPDATE activities SET timestamp=? WHERE uid=? AND aid=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setTimestamp(1, ts);
                pstmt.setInt(2, uid);
                pstmt.setInt(3, Integer.parseInt(id));
                result = pstmt.executeUpdate();
                if (result < 1) bug = true;
            }

            if (bug) {
                conn.rollback(savepoint1);
                System.out.println("ROLLBACK W BUG");
                return new WrongParametersResult("activities");
            }
            conn.commit();
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

    private String validateParameters(List<String> activities) {
        for (String id : activities) {
            try {
                Integer.parseInt(id);
            } catch (NumberFormatException e) {
                return "activities";
            }
        }
        return "";
    }
}