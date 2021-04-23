package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class PostUserHandler implements CommandHandler {

    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        String param1 = "";
        String param2 = "";

        for (String param : commandRequest.getParameters()) {
            if (param.contains("name")) param1 = param.substring(5).replace('+', ' ');
            else if (param.contains("email")) param2 = param.substring(6).replace('+', ' ');
        }

        if (param1.equals("") || param2.equals("")) {
            conn.close();
            return Optional.empty();
        }

        String sql = "INSERT INTO users(name, email) values(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, param1);
        pstmt.setString(2, param2);
        pstmt.executeUpdate();

        String sql1 = "SELECT MAX(uid) FROM users";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt1.executeQuery()));
        conn.close();
        return optional;
    }
}
