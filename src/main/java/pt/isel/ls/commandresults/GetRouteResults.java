package pt.isel.ls.commandresults;

import pt.isel.ls.Element;
import pt.isel.ls.Text;
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

    public String generateJson() {
        StringBuilder json = new StringBuilder("[");

        for (Route route : routes) {
            json.append("\n").append(route.generateJson()).append(",");
        }
        json.append("\n]");

        return json.toString();
    }

    public Element generateHtml() {
        Element html = html();
        Element body = body();

        html.with(head().with(title().with(new Text("Users"))));
        html.with(body.with(h1().with(new Text("User Details"))));

        for (Route route : routes) {
            body.with(ul().with(
                    li().with(new Text("Identifier : " + route.getRid())),
                    li().with(new Text("Start Location : " + route.getStartLocation())),
                    li().with(new Text("End Location : " + route.getEndLocation())),
                    li().with(new Text("Distance : " + route.getDistance()))));
        }

        return html;

    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }
}
