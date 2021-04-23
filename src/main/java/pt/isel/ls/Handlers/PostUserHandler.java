package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostUserHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        ArrayList<String> pathParameters = commandRequest.getPathParameters();

        String param1=commandRequest.getParameters().get(0).substring(5).replace('+',' ');
        String param2=commandRequest.getParameters().get(1).substring(6).replace('+',' ');

        String sql = "INSERT INTO users(email,name) values(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //pstmt.setInt(1, 916);
        pstmt.setString(1, param1);
        pstmt.setString(2, param2);
        pstmt.executeUpdate();

        String sql1 = "SELECT * FROM users";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        return new CommandResult(pstmt1.executeQuery());
    }
}
