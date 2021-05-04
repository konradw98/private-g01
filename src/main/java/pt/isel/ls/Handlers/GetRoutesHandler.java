package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResults.CommandResult;
import pt.isel.ls.CommandResults.GetRouteResults;
import pt.isel.ls.CommandResults.GetUsersResult;
import pt.isel.ls.CommandResults.WrongParametersResult;
import pt.isel.ls.Models.Route;
import pt.isel.ls.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetRoutesHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        try {

            String sql = "SELECT * FROM routes";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            conn.close();

            int rid;
            String start_location, end_location;
            Route route;
            double distance;
            ArrayList<Route> routes = new ArrayList<>();

            while (resultSet.next()) {
                rid = resultSet.getInt("rid");
                start_location = resultSet.getString("start_location");
                end_location = resultSet.getString("end_location");
                distance = resultSet.getDouble("distance");
                route = new Route(rid, start_location, end_location, distance);
                routes.add(route);
            }
            if (routes.size() == 0) {
                return new WrongParametersResult();
            } else return new GetRouteResults(routes);

        } finally {
            conn.close();
        }
    }
}
