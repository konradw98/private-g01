package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Route;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GetRoutesResult extends GetCommandResult {
    private final ArrayList<Route> routes;
    private Headers headers;

    public GetRoutesResult(ArrayList<Route> routes, Headers headers) {
        this.headers = headers;
        this.routes = routes;
    }

    public void generateResult(Headers headers) {
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
                case "text/plain" -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Route route : routes) {
                        stringBuilder.append(route.toString());
                    }
                    str = stringBuilder.toString();
                }
                case "application/json" -> str = generateJson();
                default -> str = generateHtml().generateStringHtml("");
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(str);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            switch (accept) {
                case "text/plain" -> {
                    for (Route route : routes) {
                        System.out.println(route);
                    }
                }
                case "application/json" -> System.out.println(generateJson());
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
                th().with(new Text("Identifier")),
                th().with(new Text("Start Location")),
                th().with(new Text("End Location")),
                th().with(new Text("Distance"))));

        for (Route route : routes) {
            table.with(tr().with(
                    td().with(new Text(route.getRid())),
                    td().with(new Text(route.getStartLocation())),
                    td().with(new Text(route.getEndLocation())),
                    td().with(new Text(route.getDistance()))));
        }

        return html;

    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
