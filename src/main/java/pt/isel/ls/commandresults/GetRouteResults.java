package pt.isel.ls.commandresults;

import pt.isel.ls.models.Route;
import pt.isel.ls.models.User;

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

    public String generateJSON() {
        String json = "[";

        for (Route route : routes) {
            json = json + "\n" + route.generateJSON() + ",";
        }
        json = json + "\n]";

        return json;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }
}
