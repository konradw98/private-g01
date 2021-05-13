package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Route;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetRouteResult extends GetCommandResult {
    private Route route;
    private Headers headers;

    public GetRouteResult(Route route, Headers headers) {
        this.headers = headers;
        this.route = route;
    }

    public void generateResult(Headers headers) {
        String accept;
        String fileName;
        if (headers == null) {
            accept = "text/html";
            fileName = null;
        } else {
            accept = headers.get("accept");
            fileName = headers.get("file-name");
        }

        if (fileName != null) {
            switch (accept) {
                case "text/plain" -> {
                    try {
                        String str = route.toString();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                        writer.write(str);
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "application/json" -> {
                    try {
                        String str = generateJson();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                        writer.write(str);
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                default -> {
                    try {
                        String str = generateHtml().generateStringHtml("");
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                        writer.write(str);
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            switch (accept) {
                case "text/plain" -> {
                    System.out.println(route);
                }
                case "application/json" -> {
                    System.out.println(generateJson());
                }
                default -> System.out.println(generateHtml().generateStringHtml(""));
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
