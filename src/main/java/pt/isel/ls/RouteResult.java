package pt.isel.ls;

import pt.isel.ls.Handlers.CommandHandler;

import java.util.ArrayList;

public class RouteResult {
    private final CommandHandler handler;
    private ArrayList<String> parameters;

    public RouteResult(CommandHandler handler, ArrayList<String> parameters) {
        this.handler = handler;
        this.parameters = parameters;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }
}
