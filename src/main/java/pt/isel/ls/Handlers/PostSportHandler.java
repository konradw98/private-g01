package pt.isel.ls.Handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.CommandResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class PostSportHandler implements CommandHandler {

    @Override
    public Optional<CommandResult> execute(CommandRequest commandRequest) throws SQLException {
        Connection conn = commandRequest.getDataSource().getConnection();

        String name = "";
        String description = "";

        for (String param : commandRequest.getParameters()) {
            if (param.contains("name")) name = param.substring(5).replace('+', ' ');
            else if (param.contains("description")) description = param.substring(12).replace('+', ' ');
        }

        String wrongParameters = checkParameters(name, description);

        if (!wrongParameters.equals("")) {
            conn.close();
            System.out.println("Wrong parameters:" + wrongParameters);
            return Optional.empty();
        }

        String sql = "INSERT INTO sports(name,description) values(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.executeUpdate();

        String sql1 = "SELECT MAX(sid) FROM sports";
        PreparedStatement pstmt1 = conn.prepareStatement(sql1);
        Optional<CommandResult> optional = Optional.of(new CommandResult(pstmt1.executeQuery()));
        conn.close();
        return optional;
    }

    private String checkParameters(String name, String description) {
        String wrongParameters = "";
        if (name.equals("")) {
            wrongParameters += " name = " + name;
        }

        if (description.equals("")) {
            wrongParameters += " description = " + description;
        }

        return wrongParameters;
    }
}
