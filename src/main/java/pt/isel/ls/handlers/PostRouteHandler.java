package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.PostResult;
import pt.isel.ls.commandresults.WrongParametersResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRouteHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Parameters parameters = commandRequest.getParameters();
        String startLocation = parameters.get("startLocation");
        String endLocation = parameters.get("endLocation");
        String distance = parameters.get("distance");

        String wrongParameters = checkParameters(startLocation, endLocation, distance);
        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        try (Connection conn = commandRequest.getDataSource().getConnection()) {
            String sql = "INSERT INTO routes(start_location, end_location, distance) values(?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, startLocation);
            pstmt.setString(2, endLocation);
            pstmt.setFloat(3, Float.parseFloat(distance));
            pstmt.executeUpdate();

            String sql1 = "SELECT MAX(rid) FROM routes";
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            ResultSet resultSet = pstmt1.executeQuery();
            if (resultSet.next()) {
                int rid = resultSet.getInt("max");
                return new PostResult(rid, "rid");
            } else {
                return new WrongParametersResult(wrongParameters);
            }

        }

    }

    private String checkParameters(String startLocation, String endLocation, String distance) {
        String wrongParameters = "";
        if (startLocation == null) {
            wrongParameters += "start location ";
        }

        if (endLocation == null) {
            wrongParameters += "end location ";
        }

        float distanceInt;
        try {
            distanceInt = Float.parseFloat(distance);
        } catch (NumberFormatException | NullPointerException e) {
            return wrongParameters + "distance ";
        }

        if (distanceInt <= 0) {
            wrongParameters += "distance ";
        }
        return wrongParameters;
    }
}