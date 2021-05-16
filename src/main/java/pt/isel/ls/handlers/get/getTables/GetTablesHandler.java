package pt.isel.ls.handlers.get.getTables;

import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.handlers.get.GetHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public abstract class GetTablesHandler extends GetHandler implements CommandHandler {

    protected String validateParameters(String skip, String top) {
        String wrongParameters = "";
        int skipInt;
        try {
            skipInt = Integer.parseInt(skip);
        } catch (NumberFormatException e) {
            return wrongParameters + "skip ";
        }
        if (skipInt < 0) {
            wrongParameters += "skip ";
        }
        int topInt;
        try {
            topInt = Integer.parseInt(top);
        } catch (NumberFormatException e) {
            return wrongParameters + "top ";
        }
        if (topInt < 1) {
            wrongParameters += "top ";
        }
        return wrongParameters;
    }

    protected Optional<EmptyTableResult> checkIfTableIsEmpty(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        int count = 1;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        if (count == 0) {
            return Optional.of(new EmptyTableResult("users"));
        }
        return Optional.empty();
    }
}
