package pt.isel.ls.handlers.get.gettables;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.*;
import pt.isel.ls.commandresults.getresult.GetSportsResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Sport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetSportsHandler extends GetTablesHandler implements CommandHandler {


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
            Optional<EmptyTableResult> emptyTableResult = checkIfTableIsEmpty(conn, "sports");
            if (emptyTableResult.isPresent()) {
                return emptyTableResult.get();
            }

            int maxSid = 0;

            String sql1 = "SELECT MAX(sid) FROM sports";
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            ResultSet resultSet = pstmt1.executeQuery();
            if (resultSet.next()) {
                maxSid = resultSet.getInt("max");
            } else {
                return new WrongParametersResult(wrongParameters);
            }

            String sql2 = "SELECT * FROM sports ORDER BY sid";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            resultSet = pstmt2.executeQuery();

            int sid;
            String name;
            String description;
            Sport sport;
            ArrayList<Sport> sports = new ArrayList<>();

            int i = 0;
            while (resultSet.next()) {
                if (i >= skipInt && i < skipInt + Integer.parseInt(top)) {
                    sid = resultSet.getInt("sid");
                    name = resultSet.getString("name");
                    description = resultSet.getString("description");
                    sport = new Sport(sid, name, description);
                    sports.add(sport);
                }
                i++;
            }
            if (sports.size() == 0) {
                return new WrongParametersResult();
            } else {
                return new GetSportsResult(sports, maxSid, commandRequest.getHeaders(), commandRequest.getParameters());
            }
        }
    }
}
