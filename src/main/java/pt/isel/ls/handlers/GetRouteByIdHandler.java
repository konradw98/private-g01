package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.GetRouteResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.models.Route;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetRouteByIdHandler implements CommandHandler {
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
                String startLocation = resultSet.getString("start_location");
                String endLocation = resultSet.getString("end_location");
                double distance = resultSet.getDouble("distance");
                Route route = new Route(rid, startLocation, endLocation, distance);
                return new GetRouteResult(route);
            } else {
                return new WrongParametersResult();
            }
        } finally {
            conn.close();
        }
    }
}