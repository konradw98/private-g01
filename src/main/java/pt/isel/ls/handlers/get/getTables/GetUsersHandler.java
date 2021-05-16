package pt.isel.ls.handlers.get.getTables;

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
        if (!commandRequest.hasParameters()) {
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

        Connection conn = commandRequest.getDataSource().getConnection();

        try {
            Optional<EmptyTableResult> emptyTableResult = checkIfTableIsEmpty(conn);
            if (emptyTableResult.isPresent()) {
                conn.close();
                return emptyTableResult.get();
            }

            String sql1 = "SELECT * FROM users WHERE uid BETWEEN ? AND ?";
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            pstmt.setInt(1, skipInt);
            pstmt.setInt(2, Integer.parseInt(top) + skipInt - 1);
            ResultSet resultSet = pstmt.executeQuery();
            conn.close();

            int uid;
            String name;
            String email;
            User user;
            ArrayList<User> users = new ArrayList<>();

            while (resultSet.next()) {
                uid = resultSet.getInt("uid");
                name = resultSet.getString("name");
                email = resultSet.getString("email");
                user = new User(uid, email, name);
                users.add(user);
            }
            if (users.size() == 0) {
                return new WrongParametersResult();
            } else {
                return new GetUsersResult(users, commandRequest.getHeaders());
            }

        } finally {
            conn.close();
        }
    }
}
