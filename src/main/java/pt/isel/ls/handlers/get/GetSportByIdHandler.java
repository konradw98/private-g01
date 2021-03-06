package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.ResourcesNotFoundResult;
import pt.isel.ls.commandresults.getresult.GetSportResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Sport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSportByIdHandler extends GetHandler implements CommandHandler {

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        String stringSid = commandRequest.getPathParameters().get("sid");
        String wrongParameters = validatePathParameters(stringSid);

        Headers headers = commandRequest.getHeaders();
        wrongParameters += validateHeaders(headers);

        Parameters parameters = commandRequest.getParameters();
        wrongParameters += validateParameters(parameters);

        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        try (Connection conn = commandRequest.getDataSource().getConnection()) {
            String sql = "SELECT COUNT(*) FROM sports";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            int count = 1;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count == 0) {
                return new EmptyTableResult("sports");
            }

            String sql1 = "SELECT * FROM sports WHERE sid=?";
            pstmt = conn.prepareStatement(sql1);
            pstmt.setInt(1, Integer.parseInt(stringSid));
            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                int sid = resultSet.getInt("sid");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Sport sport = new Sport(sid, name, description);
                return new GetSportResult(sport, commandRequest.getHeaders());
            } else {
                return new ResourcesNotFoundResult();
            }
        }
    }

    private String validatePathParameters(String sid) {
        String wrongParameters = "";
        int sidInt;
        try {
            sidInt = Integer.parseInt(sid);
        } catch (NumberFormatException | NullPointerException e) {
            return wrongParameters + "sid ";
        }
        if (sidInt < 1) {
            wrongParameters += "sid ";
        }
        return wrongParameters;
    }

    private String validateParameters(Parameters parameters) {
        if (parameters != null) {
            return "no parameters are needed ";
        } else {
            return "";
        }
    }
}
