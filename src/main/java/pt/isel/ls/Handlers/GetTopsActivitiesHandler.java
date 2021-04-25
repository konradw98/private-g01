package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetTopsActivitiesHandler implements CommandHandler {

    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {

        Connection conn = commandRequest.getDataSource().getConnection();

        int paramSid = Integer.parseInt(commandRequest.getParameters().get(0).substring(4));
        String paramOrderBy = commandRequest.getParameters().get(1).substring(8);
        PreparedStatement pstmt;

        ArrayList<String> parameters = commandRequest.getParameters();
        if (parameters.size() == 4) {
            Date paramDate = Date.valueOf(commandRequest.getParameters().get(2).substring(5));
            int paramRid = Integer.parseInt(commandRequest.getParameters().get(3).substring(4));

            String sql = "SELECT * FROM activities WHERE sid=? AND date=? AND rid=? ORDER BY duration_time "+paramOrderBy;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(3, paramRid);
            pstmt.setDate(2, paramDate);
            pstmt.setInt(1, paramSid);
            pstmt.setString(4,paramOrderBy);
            Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt.executeQuery()));
            conn.close();
            return optional;
        } else if (parameters.size() == 3) {
            if (parameters.get(3).charAt(0) == 'd') {
                Date paramDate = Date.valueOf(commandRequest.getParameters().get(2).substring(5));
                String sql = "SELECT * FROM activities WHERE sid=? AND date=? ORDER BY duration_time "+paramOrderBy;
                pstmt = conn.prepareStatement(sql);
                pstmt.setDate(2, paramDate);
                pstmt.setInt(1, paramSid);
                pstmt.setString(4,paramOrderBy);
                Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt.executeQuery()));
                conn.close();
                return optional;
            }
            else if (parameters.get(3).charAt(0) == 'r') {
                int paramRid = Integer.parseInt(commandRequest.getParameters().get(3).substring(4));
                String sql = "SELECT * FROM activities WHERE sid=? AND rid=? ORDER BY duration_time "+paramOrderBy;
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(2, paramRid);
                pstmt.setInt(1, paramSid);
                pstmt.setString(4,paramOrderBy);
                Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt.executeQuery()));
                conn.close();
                return optional;
            }
        } else if (parameters.size() == 2) {
            String sql = "SELECT * FROM activities WHERE sid=? ORDER BY duration_time "+paramOrderBy;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, paramSid);
            pstmt.setInt(1, paramSid);
            pstmt.setString(4,paramOrderBy);
            Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt.executeQuery()));
            conn.close();
            return optional;
        }
        conn.close();
        return Optional.empty();
    }
}