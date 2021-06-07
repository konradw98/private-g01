package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.Text;
import pt.isel.ls.models.Activity;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GetActivitiesResult extends GetCommandResult {
    private ArrayList<Activity> activities;
    private final Headers headers;
    private int skip;
    private int top;


    public GetActivitiesResult(ArrayList<Activity> activities, Headers headers) {
        this.headers = headers;
        this.activities = activities;
    }

    public GetActivitiesResult(ArrayList<Activity> activities, Headers headers, Parameters parameters) {
        this.headers = headers;
        this.activities = activities;
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
                    for (Activity activity : activities) {
                        stringBuilder.append(activity.toString());
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
                    for (Activity activity : activities) {
                        stringBuilder.append(activity);
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

        for (Activity activity : activities) {
            json.append("\n").append(activity.generateJson()).append(",");
        }
        json.deleteCharAt(json.length() - 1);
        json.append("\n]");

        return json.toString();
    }

    public Element generateHtml() {
        Element html = html();
        Element table = table();

        html.with(head().with(title().with(new Text("Activities"))));
        html.with(body().with(table));
        table.with(h1().with(new Text("Activity Details")));
        table.with(tr().with(
                th().with(new Text("Identifier : ")),
                th().with(new Text("Date : ")),
                th().with(new Text("Duration Time : ")),
                th().with(new Text("Sport Id : ")),
                th().with(new Text("User Id : ")),
                th().with(new Text("Route Id : "))));

        for (Activity activity : activities) {
            table.with(tr().with(
                    td().with(new Text(activity.getAid())),
                    td().with(new Text(activity.getDate())),
                    td().with(new Text(activity.getDurationTime())),
                    td().with(new Text(activity.getSid())),
                    td().with(new Text(activity.getUid())),
                    td().with(new Text(activity.getRid()))));
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

        html.with(head().with(title().with(new Text("Activities"))));
        body.with(a("href=\"/\"").with(new Text("Root")));
        html.with(body.with(table));
        table.with(h1().with(new Text("Activities Page " + pageNumber)));

        table.with(tr().with(
                th().with(new Text("Identifier")),
                th().with(new Text("Date"))));

        for (Activity activity : activities) {
            table.with(tr().with(
                    td().with(a("href=\"/sports/" + activity.getSid() + "/activities/"
                            + activity.getAid() + "\"").with(new Text(activity.getAid()))),
                    td().with(new Text(activity.getDate()))));
        }

        if (activities.size() == 5) {
            body.with(a("href=\"/sports/" + activities.get(0).getSid() + "/activities?top=5&skip="
                    + (skip + 5) + "\"").with(new Text("Next")));
        }
        if (skip >= 5) {
            body.with(a("href=\"/sports/" + activities.get(0).getSid() + "/activities?top=5&skip="
                    + (skip - 5) + "\"").with(new Text("Previous")));
        }
        return html;

    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}
