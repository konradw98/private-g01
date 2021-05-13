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
        Connection conn = commandRequest.getDataSource().getConnection();
        conn.setAutoCommit(false);
        Savepoint savepoint1 = conn.setSavepoint("Savepoint1");
        int uid = Integer.parseInt(commandRequest.getPathParameters().get("uid"));
        List<String> activities = new ArrayList<>();


        try {

            for (int i = 0; i < commandRequest.getParameters().size(); i++) {
                activities.add(commandRequest.getParameters().get("activity" + i));
            }

            java.util.Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());

            for (String id : activities) {

                String sql = "UPDATE activities SET timestamp=? WHERE uid=? AND aid=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setTimestamp(1, ts);
                pstmt.setInt(2, uid);
                pstmt.setInt(3, Integer.parseInt(id));
                pstmt.executeUpdate();
                conn.commit();
            }


        } catch (SQLException sql) {
            conn.rollback(savepoint1);
            return new WrongParametersResult();
        } finally {
            conn.close();
        }


        return new DeleteResult(uid, activities);
    }
}