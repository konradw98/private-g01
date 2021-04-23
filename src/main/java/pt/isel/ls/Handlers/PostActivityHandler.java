package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.*;
import java.util.Optional;

public class PostActivityHandler implements CommandHandler {
    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        int paramUid = 0;
        int paramTime = -1;
        Date paramDate = null;

        for (String param : commandRequest.getParameters()) {
            if (param.contains("uid")) paramUid = Integer.parseInt(param.substring(4).replace('+', ' '));
            else if (param.contains("time")) paramTime = Integer.parseInt(param.substring(9).replace('+', ' '));
            else if (param.contains("date")) paramDate = Date.valueOf(param.substring(5).replace('+', ' '));
        }

        int paramSid = Integer.parseInt(commandRequest.getPathParameters().get(0));

        String sql;
        PreparedStatement pstmt;
        if (commandRequest.getParameters().size() == 4) {
            sql = "INSERT INTO activities(date,duration_time,sid,uid,rid) values(?,?,?,?,?)";
            int paramRid = 0;
            for (String param : commandRequest.getParameters()) {
                if (param.contains("rid")) paramRid = Integer.parseInt(param.substring(4));
            }
            if (paramRid == 0) return Optional.empty();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(5, paramRid);
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
        return Optional.of(new CommandResult(pstmt1.executeQuery()));
    }
}
