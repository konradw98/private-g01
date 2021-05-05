package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
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
        Connection conn = commandRequest.getDataSource().getConnection();

        try {

            String startLocation = "";
            String endLocation = "";
            float distance = -1;

            for (String param : commandRequest.getParameters()) {
                if (!param.contains("startLocation")) {
                    if (param.contains("endLocation")) {
                        endLocation = param.substring(12).replace('+', ' ');
                    } else if (param.contains("distance")) {
                        distance = Float.parseFloat(param.substring(9).replace('+', ' '));
                    }
                } else {
                    startLocation = param.substring(14).replace('+', ' ');
                }
            }

            String wrongParameters = checkParameters(startLocation, endLocation, distance);
            if (!wrongParameters.equals("")) {
                conn.close();
                return new WrongParametersResult(wrongParameters);
            }

            String sql = "INSERT INTO routes(start_location, end_location, distance) values(?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, startLocation);
            pstmt.setString(2, endLocation);
            pstmt.setFloat(3, distance);
            pstmt.executeUpdate();

            String sql1 = "SELECT MAX(rid) FROM routes";
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            ResultSet resultSet = pstmt1.executeQuery();
            conn.close();
            if (resultSet.next()) {
                int rid = resultSet.getInt("max");
                return new PostResult(rid, "rid");
            } else {
                return new WrongParametersResult(wrongParameters);
            }

        } finally {
            conn.close();
        }

    }

    private String checkParameters(String startLocation, String endLocation, float distance) {
        String wrongParameters = "";
        if (startLocation.equals("")) {
            wrongParameters += " start location";
        }

        if (endLocation.equals("")) {
            wrongParameters += " end location";
        }

        if (distance < 0) {
            wrongParameters += " distance = " + distance;
        }
        return wrongParameters;
    }
}