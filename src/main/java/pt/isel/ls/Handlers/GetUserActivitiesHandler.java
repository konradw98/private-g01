package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetUserActivitiesHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        ArrayList<String> parameters = commandRequest.getParameters();

        String sql = "SELECT * FROM activities WHERE uid=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, Integer.parseInt(parameters.get(0)));
        return new CommandResult(pstmt.executeQuery());

    }
}