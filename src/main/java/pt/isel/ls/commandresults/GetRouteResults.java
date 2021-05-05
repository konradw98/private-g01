package pt.isel.ls.commandresults;

import pt.isel.ls.models.Route;
import java.util.ArrayList;

public class GetRouteResults implements CommandResult {
    private ArrayList<Route> routes;

    public GetRouteResults(ArrayList<Route> routes) {
        this.routes = routes;
    }

    @Override
    public void print() {
        for (Route route : routes) {
            route.print();
        }
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }
}
