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
            json.append("\n").append(route.generateJSON()).append(",");
        }
        json.append("\n]");

        return json.toString();
    }

    public Element generateHtml() {
        Element html = html();
        Element body = body();

        html.with(head().with(title().with(new Text("Users"))), head());
        html.with(body.with(h1().with(new Text("User Details")),
                h1()),
                body());

        for(Route route: routes)
        {
            body.with(ul().with(
                    li().with(new Text("Identifier : " + route.getRid())),li(),
                    li().with(new Text("Start Location : " + route.getStartLocation())),li(),
                    li().with(new Text("End Location : " + route.getEndLocation())),li(),
                    li().with(new Text("Distance : " + route.getDistance())),li()),
                    ul());
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
