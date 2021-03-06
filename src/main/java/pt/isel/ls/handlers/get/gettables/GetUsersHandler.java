package pt.isel.ls.handlers.get.gettables;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.getresult.GetUsersResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class GetUsersHandler extends GetTablesHandler implements CommandHandler {
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
            Optional<EmptyTableResult> emptyTableResult = checkIfTableIsEmpty(conn, "users");
            if (emptyTableResult.isPresent()) {
                return emptyTableResult.get();
            }

            String sql1 = "SELECT MAX(uid) FROM users";
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            ResultSet resultSet = pstmt1.executeQuery();

            int maxUid = 0;

            if (resultSet.next()) {
                maxUid = resultSet.getInt("max");
            } else {
                return new WrongParametersResult(wrongParameters);
            }

            String sql2 = "SELECT * FROM users ORDER BY uid";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            resultSet = pstmt2.executeQuery();

            int uid;
            String name;
            String email;
            User user;
            ArrayList<User> users = new ArrayList<>();

            int i = 0;
            while (resultSet.next()) {
                if (i >= skipInt && i < skipInt + Integer.parseInt(top)) {
                    uid = resultSet.getInt("uid");
                    name = resultSet.getString("name");
                    email = resultSet.getString("email");
                    user = new User(uid, email, name);
                    users.add(user);
                }
                i++;
            }
            if (users.size() == 0) {
                return new WrongParametersResult();
            } else {
                return new GetUsersResult(users, maxUid, commandRequest.getHeaders(), commandRequest.getParameters());
            }
        }
    }
}
