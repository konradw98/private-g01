package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Activity;
import pt.isel.ls.models.Route;
import pt.isel.ls.models.Sport;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GetRouteResult extends GetCommandResult {
    private final Route route;
    private Headers headers;
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<Sport> sports = new ArrayList<>();

    public GetRouteResult(Route route, Headers headers) {
        this.headers = headers;
        this.route = route;
    }

    public GetRouteResult(Route route, Headers headers, ArrayList<Sport> sports, ArrayList<Activity> activities) {
        this.headers = headers;
        this.route = route;
        this.sports = sports;
        this.activities = activities;
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
                case "text/plain" -> str = route.toString();
                case "application/json" -> str = generateJson();
                default -> str = http ? generateHtmlWithLinks().generateStringHtml("")
                        : generateHtmlWithLinks().generateStringHtml("");
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
                    return http ? generateHtmlWithLinks().generateStringHtml("")
                            : generateHtmlWithLinks().generateStringHtml("");
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

    public Element generateHtmlWithLinks() {
        Element html = html();
        html.with(head().with(title().with(new Text("Route"))));

        Element body = body();
        html.with(body);
        body.with(aa("href=\"/\"").with(new Text("Root")));
        body.with(aa("href=\"/routes?top=5&skip=0\"").with(new Text("Routes")));
        body.with(h1().with(new Text("Route Details")),
                ul().with(
                        li().with(new Text("Identifier : " + route.getRid())),
                        li().with(new Text("Distance : " + route.getDistance())),
                        li().with(new Text("Start location : " + route.getStartLocation())),
                        li().with(new Text("End location : " + route.getEndLocation()))));

        body.with(h2().with(new Text("Sports")));

        Element table1 = table("border=1");
        body.with(table1);
        table1.with(tr().with(
                th().with(new Text("Identifier")),
                th().with(new Text("Name"))));

        for (Sport sport : sports) {
            table1.with(tr().with(
                    td().with(aa("href=\"/sports/" + sport.getSid() + "\"").with(new Text(sport.getSid()))),
                    td().with(new Text(sport.getName()))));
        }

        body.with(h2().with(new Text("Activities")));
        Element table2 = table("border=1");
        body.with(table2);

        table2.with(tr().with(
                th().with(new Text("Number")),
                th().with(new Text("Sport")),
                th().with(new Text("Date"))));

        for (Activity activity : activities) {
            table2.with(tr().with(
                    td().with(aa("href=\"/sports/" + activity.getSid() + "/activities/" + activity.getAid()
                            + "\"").with(new Text(activity.getAid()))),
                    td().with(new Text(getSportName(activity.getSid()))),
                    td().with(new Text(activity.getDate()))));
        }
        return html;
    }


    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getSportName(int sid) {
        for (Sport sport : sports) {
            if (sport.getSid() == sid) {
                return sport.getName();
            }
        }
        return null;
    }
}
