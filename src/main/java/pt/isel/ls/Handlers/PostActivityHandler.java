package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PostActivityHandler implements CommandHandler{
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        int paramUid=Integer.parseInt(commandRequest.getParameters().get(0).substring(4));
        //Time paramTime=Time.valueOf(commandRequest.getParameters().get(1).substring(9));
        int paramTime=Integer.parseInt(commandRequest.getParameters().get(1).substring(9));
        Date paramDate=Date.valueOf(commandRequest.getParameters().get(2).substring(5));
        int paramSid=Integer.parseInt(commandRequest.getPathParameters().get(0));
        int paramRid=Integer.parseInt(commandRequest.getParameters().get(3).substring(4));

        String sql = "INSERT INTO activities(date,duration_time,sid,uid,rid) values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setDate(1,paramDate);
        //pstmt.setTime(2,paramTime);
        pstmt.setInt(2,paramTime);
        pstmt.setInt(3,paramSid);
        pstmt.setInt(4,paramUid);
        pstmt.setInt(5,paramRid);
        pstmt.executeUpdate();

        String sql1 = "SELECT MAX(aid) FROM activities";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        return new CommandResult(pstmt1.executeQuery());
    }
}
