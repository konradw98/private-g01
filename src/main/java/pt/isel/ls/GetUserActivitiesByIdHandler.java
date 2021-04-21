package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetUserActivitiesByIdHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        ArrayList<String> parameters = commandRequest.getParameters();

        String sql = "SELECT * FROM activity WHERE uid=? AND aid=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, Integer.parseInt(parameters.get(0)));
        pstmt.setInt(2, Integer.parseInt(parameters.get(1)));
        return new CommandResult(pstmt.executeQuery());
    }

}
