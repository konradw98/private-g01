package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.getresult.GetRouteResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Route;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetRouteByIdHandler extends GetHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        String stringRid = commandRequest.getPathParameters().get("rid");
        String wrongParameters = validatePathParameters(stringRid);

        Headers headers = commandRequest.getHeaders();
        wrongParameters += validateHeaders(headers);

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



            String sql1 = "SELECT * FROM routes WHERE rid=?";
            pstmt = conn.prepareStatement(sql1);
            pstmt.setInt(1, Integer.parseInt(stringRid));
            resultSet = pstmt.executeQuery();
            conn.close();
            if (resultSet.next()) {
                int rid = resultSet.getInt("rid");
                String startLocation = resultSet.getString("start_location");
                String endLocation = resultSet.getString("end_location");
                double distance = resultSet.getDouble("distance");
                Route route = new Route(rid, startLocation, endLocation, distance);
                return new GetRouteResult(route, commandRequest.getHeaders());
            } else {
                return new WrongParametersResult();
            }
        } finally {
            conn.close();
        }
    }

    private String validatePathParameters(String rid) {
        String wrongParameters = "";
        int ridInt;
        try {
            ridInt = Integer.parseInt(rid);
        } catch (NumberFormatException e) {
            return wrongParameters + "rid ";
        }
        if (ridInt < 1) {
            wrongParameters += "rid ";
        }
        return wrongParameters;
    }
}
