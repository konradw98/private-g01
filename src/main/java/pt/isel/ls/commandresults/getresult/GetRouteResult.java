package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Route;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetRouteResult extends GetCommandResult {
    private final Route route;
    private Headers headers;

    public GetRouteResult(Route route, Headers headers) {
        this.headers = headers;
        this.route = route;
    }

    @Override
    public boolean results() {
        printResults(generateResults());
        return false;
    }

    @Override
    public String generateResults() {
        String accept;
        String fileName;
        if (headers == null) {
            accept = "text/html";
            fileName = null;
        } else {
            accept = headers.get("accept") == null ? "text/html" : headers.get("accept");
            fileName = headers.get("file-name");
        }

        if (fileName != null) {
            String str;
            switch (accept) {
                case "text/plain" -> str = route.toString();
                case "application/json" -> str = generateJson();
                default -> str = generateHtml().generateStringHtml("");
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(str);
                writer.close();
            } catch (Exception e) {
                System.out.println("Couldn't write to given file");
            }
        } else {
            switch (accept) {
                case "text/plain" -> {
                    return route.toString();
                }
                case "application/json" -> {
                    return generateJson();
                }
                default -> {
                    return generateHtml().generateStringHtml("");
                }
            }
        }
        return "";
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

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
