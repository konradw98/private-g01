package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;
import pt.isel.ls.CommandResults.GetRouteResult;
import pt.isel.ls.CommandResults.GetUserByIdResult;
import pt.isel.ls.CommandResults.WrongParametersResult;
import pt.isel.ls.Models.Route;
import pt.isel.ls.Models.User;
import pt.isel.ls.RouteResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetRouteByIDHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {


        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            ArrayList<String> parameters = commandRequest.getPathParameters();
            String sql = "SELECT * FROM routes WHERE rid=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(parameters.get(0)));
            ResultSet resultSet = pstmt.executeQuery();
            conn.close();
            if (resultSet.next()) {
                int rid = resultSet.getInt("rid");
                String start_location = resultSet.getString("start_location");
                String end_location = resultSet.getString("end_location");
                double distance = resultSet.getDouble("distance");
                Route route = new Route(rid, start_location, end_location, distance);
                return new GetRouteResult(route);
            } else return new WrongParametersResult();
        } finally {
            conn.close();
        }
    }
}
