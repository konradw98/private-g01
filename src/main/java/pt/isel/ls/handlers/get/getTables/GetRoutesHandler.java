package pt.isel.ls.handlers.get.getTables;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.commandresults.getresult.GetRoutesResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Route;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetRoutesHandler extends GetTablesHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        if (!commandRequest.hasParameters()) {
            return new WrongParametersResult("skip and top missing");
        }

        Parameters parameters = commandRequest.getParameters();
        String skip = parameters.get("skip");
        String top = parameters.get("top");

        String wrongParameters = validateParameters(skip, top);
        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }
        int skipInt = Integer.parseInt(skip) + 1;

        Headers headers = commandRequest.getHeaders();
        String acceptArgument = headers.get("accept");
        String fileNameArgument = headers.get("file-name");

        wrongParameters = validateHeaders(acceptArgument, fileNameArgument);
        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM routes";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            int count = 1;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count == 0) {
                return new EmptyTableResult("routes");
            }

            String sql1 = "SELECT * FROM routes WHERE rid BETWEEN ? AND ?";
            pstmt = conn.prepareStatement(sql1);
            pstmt.setInt(1, skipInt);
            pstmt.setInt(2, Integer.parseInt(top) + skipInt - 1);
            resultSet = pstmt.executeQuery();
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
                return new GetRoutesResult(routes, commandRequest.getHeaders());
            }

        } finally {
            conn.close();
        }
    }
}
