package pt.isel.ls;

import java.sql.ResultSet;

public class CommandResult {
    private ResultSet resultSet;

    public CommandResult(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
