package pt.isel.ls;

public class RouteResult {
    private final CommandHandler handler;

    public RouteResult(CommandHandler handler) {
        this.handler = handler;
    }

    public CommandHandler getHandler() {
        return handler;
    }
}
