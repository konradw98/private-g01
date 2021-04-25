package pt.isel.ls.Handlers;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetSportByIdHandler implements CommandHandler {

    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        ArrayList<String> parameters = commandRequest.getPathParameters();

        String sql1 = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();

        if (resultSet.getInt(1) < Integer.parseInt(parameters.get(0))) {
            System.out.println("Wrong parameter: sid = " + parameters.get(0));
            conn.close();
            return Optional.empty();
        }

        String sql = "SELECT * FROM sports WHERE sid=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, Integer.parseInt(parameters.get(0)));
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt.executeQuery()));
        conn.close();

        return optional;
    }
}
