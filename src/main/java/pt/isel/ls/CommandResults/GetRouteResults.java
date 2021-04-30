package pt.isel.ls.CommandResults;

import pt.isel.ls.Models.Route;
import pt.isel.ls.Models.Sport;

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
}
