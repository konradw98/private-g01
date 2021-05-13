package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Route;

public class GetRouteResult extends  GetCommandResult{
    private Route route;
    private Headers headers;

    public GetRouteResult(Route route, Headers headers) {
        this.headers=headers;
        this.route = route;
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
                    //do konsoli text plain
                }
                case "application/json" -> {
                    //do konsoli json
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
        return route.generateJson();
    }

    public Element generateHtml() {
        Element html = html();

        html.with(head().with(title().with(new Text("Routes"))));
        html.with(body().with(h1().with(new Text("Route Details")),
                            ul().with(
                                        li().with(new Text("Identifier : " + route.getRid())),
                                        li().with(new Text("Start Location : " + route.getStartLocation())),
                                        li().with(new Text("End Location : " + route.getEndLocation())),
                                        li().with(new Text("Distance : " + route.getDistance()))
                                    )
                            )
                );


        return html;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
