package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Activity;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GetActivitiesResult extends GetCommandResult {
    private ArrayList<Activity> activities;
    private final Headers headers;


    public GetActivitiesResult(ArrayList<Activity> activities, Headers headers) {
        this.headers = headers;
        this.activities = activities;
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
                    for (Activity activity : activities) {
                        stringBuilder.append(activity.toString());
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
                    for (Activity activity : activities) {
                        System.out.println(activity);
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

        for (Activity activity : activities) {
            json.append("\n").append(activity.generateJson()).append(",");
        }
        json.deleteCharAt(json.length()-1);
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

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}
