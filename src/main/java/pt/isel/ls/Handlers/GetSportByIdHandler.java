package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;
import pt.isel.ls.CommandResults.GetSportResult;
import pt.isel.ls.CommandResults.GetUserByIdResult;
import pt.isel.ls.CommandResults.WrongParametersResult;
import pt.isel.ls.Models.Sport;
import pt.isel.ls.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetSportByIdHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            ArrayList<String> parameters = commandRequest.getPathParameters();

            String sql = "SELECT * FROM sports WHERE sid=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(parameters.get(0)));
            ResultSet resultSet = pstmt.executeQuery();
            conn.close();

            if (resultSet.next()) {
                int sid = resultSet.getInt("sid");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Sport sport = new Sport(sid, name, description);
                return new GetSportResult(sport);
            } else return new WrongParametersResult();
        } finally {
            conn.close();
        }
    }
}
