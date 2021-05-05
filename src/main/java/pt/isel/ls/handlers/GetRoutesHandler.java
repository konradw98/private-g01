package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.GetRouteResults;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.models.Route;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
            String startLocation;
            String endLocation;
            Route route;
            double distance;
            ArrayList<Route> routes = new ArrayList<>();

            while (resultSet.next()) {
                rid = resultSet.getInt("rid");
                startLocation = resultSet.getString("start_location");
                endLocation = resultSet.getString("end_location");
                distance = resultSet.getDouble("distance");
                route = new Route(rid, startLocation, endLocation, distance);
                routes.add(route);
            }
            if (routes.size() == 0) {
                return new WrongParametersResult();
            } else {
                return new GetRouteResults(routes);
            }

        } finally {
            conn.close();
        }
    }
}
