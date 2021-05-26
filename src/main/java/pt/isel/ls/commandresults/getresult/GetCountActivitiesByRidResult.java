package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.models.Route;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetCountActivitiesByRidResult extends GetCommandResult implements CommandResult {

    private final Route route;
    private Headers headers;
    private final int numberOfActivities;

    public GetCountActivitiesByRidResult(Route route, int numberOfActivities, Headers headers) {
        this.route = route;
        this.headers = headers;
        this.numberOfActivities = numberOfActivities;
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
                case "text/plain" -> str = generateTextPlain();
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
                case "text/plain" -> System.out.println(generateTextPlain());
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

    //I'm not using route.generateJson() because it generates Json with rid and distance
    public String generateJson() {
        return "{ \n \"start location\": \"" + route.getStartLocation() + "\",\n \"end location\": \""
                + route.getEndLocation() + "\",\n \"number of activities\": " + numberOfActivities + "\n}";
    }

    //I'm not using route.toString() because it also displays rid and distance
    public String generateTextPlain() {
        return "start location: " + route.getStartLocation()
                + " end location: " + route.getEndLocation() + " number of activities: " + numberOfActivities;
    }

    public Element generateHtml() {
        Element html = html();

        html.with(head().with(title().with(new Text("Routes"))));
        html.with(body().with(h1().with(new Text("Route Details")),
                ul().with(
                        li().with(new Text("Start Location : " + route.getStartLocation())),
                        li().with(new Text("End Location : " + route.getEndLocation()))
                ),
                h1().with(new Text("Activity Details")),
                ul().with(
                        li().with(new Text("Number of activities with given rid : " + numberOfActivities)))
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
