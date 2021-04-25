package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class PostRouteHandler implements CommandHandler {
    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();
        String startLocation = "";
        String endLocation = "";
        int distance = -1;

        for (String param : commandRequest.getParameters()) {
            if (param.contains("startLocation")) startLocation = param.substring(14).replace('+', ' ');
            else if (param.contains("endLocation")) endLocation = param.substring(12).replace('+', ' ');
            else if (param.contains("distance")) distance = Integer.parseInt(param.substring(9).replace('+', ' '));
        }

        String wrongParameters = checkParameters(startLocation, endLocation, distance);
        if (!wrongParameters.equals("")) {
            conn.close();
            System.out.println("Wrong parameters:" + wrongParameters);
            return Optional.empty();
        }

        String sql = "INSERT INTO routes(start_location, end_location, distance) values(?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, startLocation);
        pstmt.setString(2, endLocation);
        pstmt.setInt(3, distance);
        pstmt.executeUpdate();

        String sql1 = "SELECT MAX(rid) FROM routes";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt1.executeQuery()));
        conn.close();
        return optional;
    }

    private String checkParameters(String startLocation, String endLocation, int distance) {
        String wrongParameters = "";
        if (startLocation.equals("")) {
            wrongParameters += " start location = " + startLocation;
        }

        if (endLocation.equals("")) {
            wrongParameters += " end location = " + endLocation;
        }

        if (distance < 0) {
            wrongParameters += " distance = " + distance;
        }
        return wrongParameters;
    }
}
