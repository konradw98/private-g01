package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Activity;
import pt.isel.ls.models.Sport;
import pt.isel.ls.models.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GetUserByIdResult extends GetCommandResult {
    private User user;
    private Headers headers;
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<Sport> sports = new ArrayList<>();

    public GetUserByIdResult(User user, Headers headers) {
        this.user = user;
        this.headers = headers;
    }

    public GetUserByIdResult(User user, Headers headers, ArrayList<Sport> sports, ArrayList<Activity> activities) {
        this.user = user;
        this.headers = headers;
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
                case "text/plain" -> str = user.toString();
                case "application/json" -> str = generateJson();
                default -> str = http ? generateHtmlWithLinks().generateStringHtml("")
                        : generateHtmlWithLinks().generateStringHtml("");
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
                    return user.toString();
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
        return user.generateJson();
    }

    public Element generateHtml() {
        Element html = html();
        Element body = body();

        html.with(head().with(title().with(new Text("Users"))));
        html.with(body);
        body.with(a("href=\"/\"").with(new Text("Root")));
        html.with(body.with(h1().with(new Text("User Details")),
                ul().with(
                        li().with(new Text("Identifier : " + user.getUid())),
                        li().with(new Text("Name : " + user.getName())),
                        li().with(new Text("Email : " + user.getEmail())))));

        return html;
    }

    public Element generateHtmlWithLinks() {
        Element html = html();
        Element body = body();
        Element table1 = table("border=1");
        Element table2 = table("border=1");

        html.with(head().with(title().with(new Text("User"))));
        html.with(body);
        body.with(a("href=\"/\"").with(new Text("Root")));
        body.with(a("href=\"/users?top=5&skip=0\"").with(new Text("Users")));
        body.with(h1().with(new Text("User Details")),
                ul().with(
                        li().with(new Text("Identifier : " + user.getUid())),
                        li().with(new Text("Name : " + user.getName())),
                        li().with(new Text("Email : " + user.getEmail()))));

        body.with(h2().with(new Text("Sports")));
        body.with(table1);
        table1.with(tr().with(
                th().with(new Text("Identifier")),
                th().with(new Text("Name"))));

        for (Sport sport : sports) {
            table1.with(tr().with(
                    td().with(a("href=\"/sports/" + sport.getSid() + "\"").with(new Text(sport.getSid()))),
                    td().with(new Text(sport.getName()))));
        }

        body.with(h2().with(new Text("Activities")));
        body.with(table2);

        table2.with(tr().with(
                th().with(new Text("Number")),
                th().with(new Text("Sport")),
                th().with(new Text("Date"))));

        for (Activity activity : activities) {
            table2.with(tr().with(
                    td().with(a("href=\"/sports/" + activity.getSid() + "/activities/" +activity.getAid() + "\"").
                            with(new Text(activity.getAid()))),
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

    public String getSportName(int sid){
        for(Sport sport: sports){
            if(sport.getSid() == sid) return sport.getName();
        }
        return null;
    }
}
