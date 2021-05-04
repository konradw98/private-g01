package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.*;
import pt.isel.ls.Models.Sport;
import pt.isel.ls.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetSportHandler implements CommandHandler {


    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();

        try {

            String sql = "SELECT * FROM sports";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            conn.close();


            int sid;
            String name, description;
            Sport sport;
            ArrayList<Sport> sports = new ArrayList<>();

            while (resultSet.next()) {
                sid = resultSet.getInt("sid");
                name = resultSet.getString("name");
                description = resultSet.getString("description");
                sport = new Sport(sid, name, description);
                sports.add(sport);
            }
            if (sports.size() == 0) {
                return new WrongParametersResult();
            } else return new GetSportsResult(sports);

        } finally {
            conn.close();
        }
    }
}
