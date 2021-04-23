package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetTopsActivitiesHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        ArrayList<String> parameters = commandRequest.getParameters();

        int paramSid=Integer.parseInt(commandRequest.getParameters().get(0).substring(4));
        String paramOrderBy=commandRequest.getParameters().get(1).substring(8);
        Date paramDate=Date.valueOf(commandRequest.getParameters().get(2).substring(5));
        int paramRid=Integer.parseInt(commandRequest.getParameters().get(3).substring(4));

        String sql = "SELECT * FROM activities WHERE sid=? AND date=? AND rid=? ORDER BY duration_time ASC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, paramSid);
        pstmt.setDate(2,paramDate);
        pstmt.setInt(3,paramRid);
        //pstmt.setString(4,paramOrderBy);
        return new CommandResult(pstmt.executeQuery());

    }
}
