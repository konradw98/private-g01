package pt.isel.ls;

import pt.isel.ls.Handlers.CommandHandler;

import java.util.HashMap;

public class RouteResult {
    private final CommandHandler handler;
    private final HashMap<String, String> parameters;

    public RouteResult(CommandHandler handler, HashMap<String, String> parameters) {
        this.handler = handler;
        this.parameters = parameters;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public HashMap<String, String> getPathParameters() {
        return parameters;
    }
}
