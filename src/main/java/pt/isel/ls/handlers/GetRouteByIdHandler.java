package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.PathParameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.getresult.GetRouteResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.models.Route;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetRouteByIdHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        String stringRid = commandRequest.getPathParameters().get("rid");
        String wrongParameters = validatePathParameters(stringRid);
        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            String sql = "SELECT * FROM routes WHERE rid=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(stringRid));
            ResultSet resultSet = pstmt.executeQuery();
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

    private String validatePathParameters(String uid) {
        String wrongParameters = "";
        if (uid == null || Integer.parseInt(uid) < 1) {
            wrongParameters += "sid ";
        }
        return wrongParameters;
    }
}
