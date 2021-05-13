package pt.isel.ls.handlers.gettables;

import pt.isel.ls.handlers.CommandHandler;

public abstract class GetTablesHandler implements CommandHandler {

    protected String validateParameters(String skip, String top) {
        String wrongParameters = "";
        if (skip == null || Integer.parseInt(skip) < 0) {
            wrongParameters += "skip ";
        }
        if (top == null || Integer.parseInt(top) < 0) {
            wrongParameters += " top";
        }
        return wrongParameters;
    }
}
