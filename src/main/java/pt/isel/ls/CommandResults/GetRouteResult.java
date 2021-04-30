package pt.isel.ls.CommandResults;

import pt.isel.ls.Models.Route;
import pt.isel.ls.Models.Sport;

public class GetRouteResult implements CommandResult {
    private Route route;

    public GetRouteResult(Route Route) {
        this.route = route;
    }

    @Override
    public void print() {
        route.print();
    }
}
