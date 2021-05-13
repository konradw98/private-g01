package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Route;

import java.util.ArrayList;

public class GetRouteResults extends GetCommandResult {
    private ArrayList<Route> routes;
    private Headers headers;

    public GetRouteResults(ArrayList<Route> routes, Headers headers) {
        this.headers = headers;
        this.routes = routes;
    }

    public void generateResult(Headers headers) {
        String accept = headers.get("accept");
        String fileName = headers.get("file-name");

        if (fileName != null) {
            switch (accept) {
                case "text/plain" -> {
                    //do pliki text plain
                }
                case "application/json" -> {
                    //do pliku json
                }
                case "text/html" -> {
                    //do pliku json
                }
                default -> {
                    // do pliku html
                }
            }
        } else {
            switch (accept) {
                case "text/plain" -> {
                    for (Route route : routes) {
                        System.out.println(route);
                    }
                }
                case "application/json" -> {
                    System.out.println(generateJson());

                }
                case "text/html" -> {
                    //do konsoli json
                }
                default -> {
                    // do konsoli html
                }
            }
        }

    }

    @Override
    public boolean results() {
        generateResult(headers);
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

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
