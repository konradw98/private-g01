package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Route;
import java.util.ArrayList;

public class GetRouteResults extends GetCommandResult {
    private ArrayList<Route> routes;

    public GetRouteResults(ArrayList<Route> routes, Headers headers) {
        super(headers);
        this.routes = routes;
    }

    @Override
    public boolean results() {
        for (Route route : routes) {
            route.print();
        }
        return false;
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
        Element table = table();

        html.with(head().with(title().with(new Text("Routes"))));
        html.with(body().with(table));
        table.with(h1().with(new Text("Route Details")));
        table.with(tr().with(
                th().with(new Text("Identifier : ")),
                th().with(new Text("Start Location : ")),
                th().with(new Text("End Location : ")),
                th().with(new Text("Distance : "))));

        for (Route route : routes) {
            table.with(tr().with(
                    td().with(new Text(route.getRid())),
                    td().with(new Text(route.getStartLocation())),
                    td().with(new Text(route.getEndLocation())),
                    td().with(new Text(route.getDistance()))));
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
