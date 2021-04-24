package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class PostRouteHandler implements CommandHandler {
    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {

        Connection conn = commandRequest.getDataSource().getConnection();

        String param1 = "";
        String param2 = "";
        int param3 = -1;

        for (String param : commandRequest.getParameters()) {
            if (param.contains("name")) param1 = param.substring(5).replace('+', ' ');
            else if (param.contains("email")) param2 = param.substring(6).replace('+', ' ');
            else if(param.contains("distance")) param3 = Integer.parseInt(param.substring(9).replace('+', ' '));
        }

        if (param1.equals("") || param2.equals("") || param3 < 0) {
            conn.close();
            return Optional.empty();
        }

        String sql = "INSERT INTO routes(start_location, end_location, distance) values(?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, param1);
        pstmt.setString(2, param2);
        pstmt.setInt(3, param3);
        pstmt.executeUpdate();

        String sql1 = "SELECT MAX(rid) FROM routes";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt1.executeQuery()));
        conn.close();
        return optional;
    }
}
