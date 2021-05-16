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

        int skipInt = Integer.parseInt(skip) + 1;

        Headers headers = commandRequest.getHeaders();
        wrongParameters += validateHeaders(headers);

        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        try (Connection conn = commandRequest.getDataSource().getConnection()) {
            Optional<EmptyTableResult> emptyTableResult = checkIfTableIsEmpty(conn, "routes");
            if (emptyTableResult.isPresent()) {
                return emptyTableResult.get();
            }

            String sql1 = "SELECT * FROM routes ORDER BY rid";
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            ResultSet resultSet = pstmt.executeQuery();

            int rid;
            String startLocation;
            String endLocation;
            Route route;
            double distance;
            ArrayList<Route> routes = new ArrayList<>();

            int i = 1;
            while (resultSet.next()) {
                if (i >= skipInt && i < skipInt + Integer.parseInt(top)) {
                    rid = resultSet.getInt("rid");
                    startLocation = resultSet.getString("start_location");
                    endLocation = resultSet.getString("end_location");
                    distance = resultSet.getDouble("distance");
                    route = new Route(rid, startLocation, endLocation, distance);
                    routes.add(route);
                }
            }
            if (routes.size() == 0) {
                return new WrongParametersResult();
            } else {
                return new GetRoutesResult(routes, commandRequest.getHeaders());
            }

        }
    }
}
