package pt.isel.ls;

import pt.isel.ls.handlers.CommandHandler;

public class RouteResult {
    private final CommandHandler handler;
    private final PathParameters pathParameters;

    public RouteResult(CommandHandler handler, PathParameters pathParameters) {
        this.handler = handler;
        this.pathParameters = pathParameters;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public PathParameters getPathParameters() {
        return pathParameters;
    }
}
