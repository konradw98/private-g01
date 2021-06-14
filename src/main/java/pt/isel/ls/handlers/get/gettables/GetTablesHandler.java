package pt.isel.ls.handlers.get.gettables;

import pt.isel.ls.commandresults.EmptyTableResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.handlers.get.GetHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public abstract class GetTablesHandler extends GetHandler implements CommandHandler {

    //TODO: zrob cos kurwa z tym
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

    protected Optional<EmptyTableResult> checkIfTableIsEmpty(Connection conn, String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + name + "";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        int count = 1;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        if (count == 0) {
            return Optional.of(new EmptyTableResult(name));
        }
        return Optional.empty();
    }
}
