package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.Text;
import pt.isel.ls.models.Route;
import pt.isel.ls.models.Sport;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GetRoutesResult extends GetCommandResult {
    private final ArrayList<Route> routes;
    private Headers headers;
    private int skip;
    private int top;

    public GetRoutesResult(ArrayList<Route> routes, Headers headers, Parameters parameters) {
        this.headers = headers;
        this.routes = routes;
        this.skip = Integer.parseInt(parameters.get("skip"));
        this.top = Integer.parseInt(parameters.get("top"));
    }

    @Override
    public boolean results(boolean http) {
        printResults(generateResults(http));
        return false;
    }

    @Override
    public String generateResults(boolean http) {
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
                default -> str = http ? generateHtmlWithLinks().generateStringHtml("")
                        : generateHtml().generateStringHtml("");
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
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Route route : routes) {
                        stringBuilder.append(route);
                    }
                    return stringBuilder.toString();
                }
                case "application/json" -> {
                    return generateJson();
                }
                default -> {
                    return http ? generateHtmlWithLinks().generateStringHtml("")
                            : generateHtml().generateStringHtml("");
                }
            }
        }
        return "";
    }

    public String generateJson() {
        StringBuilder json = new StringBuilder("[");

        for (Route route : routes) {
            json.append("\n").append(route.generateJson()).append(",");
        }
        json.deleteCharAt(json.length() - 1);
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

    public Element generateHtmlWithLinks() {
        int pageNumber = getPageNumber(skip, top);
        //not valid
        if (pageNumber == -1) return null;

        Element html = html();
        Element body = body();
        Element table = table("border=1");

        html.with(head().with(title().with(new Text("Routes"))));
        body.with(a("href=\"/\"").with(new Text("Root")));
        html.with(body.with(table));
        table.with(h1().with(new Text("Routes Page " + pageNumber)));

        table.with(tr().with(
                th().with(new Text("Identifier")),
                th().with(new Text("Distance"))));

        for (Route route : routes) {
            table.with(tr().with(
                    td().with(a("href=\"/routes/" + route.getRid() + "\"").with(new Text(route.getRid()))),
                    td().with(new Text(route.getDistance()))));
        }

        if (routes.size() == 5) body.with(a("href=\"/routes?top=5&skip=" + (skip + 5) + "\"").with(new Text("Next")));
        if (skip >= 5) body.with(a("href=\"/routes?top=5&skip=" + (skip - 5) + "\"").with(new Text("Previous")));
        return html;

    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
