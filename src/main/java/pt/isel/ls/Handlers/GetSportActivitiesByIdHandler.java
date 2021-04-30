package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;

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

        String wrongParameters = checkParameters(sid, aid, conn);
        if (!wrongParameters.equals("")) {
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

        return optional;
    }

    private String checkParameters(int sid, int aid, Connection conn) throws SQLException {
        String sql1 = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        ResultSet resultSet = pstmt1.executeQuery();
        resultSet.next();
        int maxSID = resultSet.getInt(1);

        sql1 = "SELECT MAX(aid) FROM activities";
        pstmt1 = conn.prepareStatement(sql1);
        resultSet = pstmt1.executeQuery();
        resultSet.next();

        String wrongParameters = "";
        if (resultSet.getInt(1) < aid) {
            wrongParameters += " aid = " + aid;
        }

        if (maxSID < sid || sid < 1) {
            wrongParameters += " sid = " + sid;
        }

        return wrongParameters;
    }
}
