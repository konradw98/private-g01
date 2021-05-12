package pt.isel.ls.commandresults;

import pt.isel.ls.models.Route;

public class GetRouteResult implements CommandResult {
    private Route route;

    public GetRouteResult(Route route) {
        this.route = route;
    }

    @Override
    public boolean results() {
        route.print();
        return true;
    }

    public String generateJson() {
        return route.generateJson();
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
