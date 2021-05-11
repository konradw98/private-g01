package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.PostResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostSportHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();

        try {

            String name = "";
            String description = "";

            for (String param : commandRequest.getParameters()) {
                if (param.contains("name")) {
                    name = param.substring(5).replace('+', ' ');
                } else if (param.contains("description")) {
                    description = param.substring(12).replace('+', ' ');
                }
            }

            String wrongParameters = checkParameters(name, description);

            if (!wrongParameters.equals("")) {
                conn.close();
                return new WrongParametersResult(wrongParameters);
            }

            String sql = "INSERT INTO sports(name,description) values(?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.executeUpdate();

            String sql1 = "SELECT MAX(sid) FROM sports";
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            ResultSet resultSet = pstmt1.executeQuery();
            conn.close();
            if (resultSet.next()) {
                int sid = resultSet.getInt("max");
                return new PostResult(sid, "sid");
            } else {
                return new WrongParametersResult(wrongParameters);
            }

        } finally {
            conn.close();
        }
    }

    private String checkParameters(String name, String description) {
        String wrongParameters = "";
        if (name.equals("")) {
            wrongParameters += " name";
        }

        if (description.equals("")) {
            wrongParameters += " description";
        }

        return wrongParameters;
    }
}