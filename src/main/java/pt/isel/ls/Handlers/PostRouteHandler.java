package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostRouteHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        //TODO: refactor
        String param1 = commandRequest.getParameters().get(0).substring(14).replace('+', ' ');
        String param2 = commandRequest.getParameters().get(1).substring(12).replace('+', ' ');
        int param3 = Integer.parseInt(commandRequest.getParameters().get(2).substring(9).replace('+', ' '));

        String sql = "INSERT INTO routes(start_location, end_location, distance) values(?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, param1);
        pstmt.setString(2, param2);
        pstmt.setInt(3, param3);
        pstmt.executeUpdate();

        String sql1 = "SELECT * FROM users";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        return new CommandResult(pstmt1.executeQuery());
    }
}
