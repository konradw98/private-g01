package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetSportHandler implements CommandHandler {


    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        //deleting student that does not exist in table yet (value 0 means no rows were affected)
        String sql = "SELECT * FROM sport";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return new CommandResult(pstmt.executeQuery());

    }
}
