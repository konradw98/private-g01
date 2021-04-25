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

public class GetSportActivitiesByIdHandler implements CommandHandler {

    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {

        Connection conn = commandRequest.getDataSource().getConnection();
        ArrayList<String> parameters = commandRequest.getPathParameters();
        int sid = Integer.parseInt(parameters.get(0));
        int aid = Integer.parseInt(parameters.get(1));

        String sql1 = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxSID = resultSet.getInt(1);

        sql1 = "SELECT MAX(aid) FROM activities";
        pstmt1 = conn.prepareStatement(sql1);
        resultSet = pstmt1.executeQuery();
        resultSet.next();

        boolean ifWrongParameters = false;
        String wrongParameters = "";
        if (resultSet.getInt(1) < aid) {
            wrongParameters += " aid = " + aid;
            ifWrongParameters = true;
        }

        if (maxSID < sid) {
            wrongParameters += " sid = " + sid;
            ifWrongParameters = true;
        }

        if (ifWrongParameters) {
            conn.close();
            System.out.println("Wrong parameter:" + wrongParameters);
            return Optional.empty();
        }

        String sql = "SELECT * FROM activities WHERE sid=? AND aid=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, sid);
        pstmt.setInt(2, aid);
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt.executeQuery()));
        conn.close();
        if (!optional.get().getResultSet().next()) {
            System.out.println("Wrong parameter: sid = " + parameters.get(0) + ", aid = " + parameters.get(1));
        }
        return optional;
    }
}
