package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.PathParameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.getresult.GetSportResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.models.Sport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSportByIdHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            PathParameters parameters = commandRequest.getPathParameters();

            String sql = "SELECT * FROM sports WHERE sid=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(parameters.get("sid")));
            ResultSet resultSet = pstmt.executeQuery();
            conn.close();

            if (resultSet.next()) {
                int sid = resultSet.getInt("sid");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Sport sport = new Sport(sid, name, description);
                return new GetSportResult(sport, commandRequest.getHeaders());
            } else {
                return new WrongParametersResult();
            }
        } finally {
            conn.close();
        }
    }
}
