package pt.isel.ls.handlers.get.getTables;

import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.handlers.get.GetHandler;

public abstract class GetTablesHandler extends GetHandler implements CommandHandler {

    protected String validateParameters(String skip, String top) {
        String wrongParameters = "";
        int skipInt;
        try {
            skipInt = Integer.parseInt(skip);
        } catch (NumberFormatException e) {
            return wrongParameters + "skip ";
        }
        if (skip == null || skipInt < 0) {
            wrongParameters += "skip ";
        }
        int topInt;
        try {
            topInt = Integer.parseInt(top);
        } catch (NumberFormatException e) {
            return wrongParameters + "top ";
        }
        if (top == null || topInt < 0) {
            wrongParameters += "top ";
        }
        return wrongParameters;
    }
}
