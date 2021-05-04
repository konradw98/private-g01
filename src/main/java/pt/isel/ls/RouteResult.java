package pt.isel.ls;

import pt.isel.ls.Handlers.CommandHandler;

import java.util.HashMap;

public class RouteResult {
    private final CommandHandler handler;
    private final HashMap<String, Integer> parameters;

    public RouteResult(CommandHandler handler, HashMap<String, Integer> parameters) {
        this.handler = handler;
        this.parameters = parameters;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public HashMap<String, Integer> getPathParameters() {
        return parameters;
    }
}
