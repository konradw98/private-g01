package pt.isel.ls.handlers.get.gettables;

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
import java.util.Optional;

public class GetRoutesHandler extends GetTablesHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        if (!commandRequest.hasPagingParameters()) {
            return new WrongParametersResult("skip and top missing");
        }

        Parameters parameters = commandRequest.getParameters();
        String skip = parameters.get("skip");
        String top = parameters.get("top");

        String wrongParameters = validateParameters(skip, top);

        Headers headers = commandRequest.getHeaders();
        wrongParameters += validateHeaders(headers);

        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        int skipInt = Integer.parseInt(skip);
        try (Connection conn = commandRequest.getDataSource().getConnection()) {
            Optional<EmptyTableResult> emptyTableResult = checkIfTableIsEmpty(conn, "routes");
            if (emptyTableResult.isPresent()) {
                return emptyTableResult.get();
            }

            int maxRid = 0;

            String sql1 = "SELECT MAX(rid) FROM routes";
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            ResultSet resultSet = pstmt1.executeQuery();
            if (resultSet.next()) {
                maxRid = resultSet.getInt("max");
            } else {
                return new WrongParametersResult(wrongParameters);
            }

            String sql2 = "SELECT * FROM routes ORDER BY rid";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            resultSet = pstmt2.executeQuery();

            int rid;
            String startLocation;
            String endLocation;
            Route route;
            double distance;
            ArrayList<Route> routes = new ArrayList<>();

            int i = 0;
            while (resultSet.next()) {
                if (i >= skipInt && i < skipInt + Integer.parseInt(top)) {
                    rid = resultSet.getInt("rid");
                    startLocation = resultSet.getString("start_location");
                    endLocation = resultSet.getString("end_location");
                    distance = resultSet.getDouble("distance");
                    route = new Route(rid, startLocation, endLocation, distance);
                    routes.add(route);
                }
                i++;
            }
            if (routes.size() == 0) {
                return new WrongParametersResult();
            } else {
                return new GetRoutesResult(routes, maxRid, commandRequest.getHeaders(), commandRequest.getParameters());
            }

        }
    }
}
