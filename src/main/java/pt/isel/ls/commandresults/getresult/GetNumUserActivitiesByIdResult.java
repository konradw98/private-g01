package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.models.User;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetNumUserActivitiesByIdResult extends GetCommandResult implements CommandResult {
    private User user;
    private Headers headers;
    private int numActivities;

    public GetNumUserActivitiesByIdResult(User user, Headers headers, int numActivities) {
        this.user = user;
        this.headers = headers;
        this.numActivities = numActivities;
    }

    @Override
    public boolean results() {
        generateResult(headers);
        return false;
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
                case "text/plain" -> str = user.toString() + " Number of activities " + numActivities;
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
                case "text/plain" -> System.out.println(user + " Number of activities " + numActivities);
                case "application/json" -> System.out.println(generateJson());
                default -> System.out.println(generateHtml().generateStringHtml(""));
            }
        }

    }

    public String generateJson() {
        return user.generateJson(numActivities);
    }

    public Element generateHtml() {
        Element html = html();
        Element body = body();

        html.with(head().with(title().with(new Text("User"))));
        html.with(body.with(h1().with(new Text("User Details")),
                ul().with(
                        li().with(new Text("Name : " + user.getName())),
                        li().with(new Text("Email : " + user.getEmail())),
                        li().with(new Text("Number of Activities : " + user.getUid())))));

        return html;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
