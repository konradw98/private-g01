package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Activity;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetActivityResult extends GetCommandResult {
    private Activity activity;
    private final Headers headers;


    public GetActivityResult(Activity activity, Headers headers) {
        this.activity = activity;
        this.headers = headers;
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
                    str = activity.toString();
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
                    return activity.toString();
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
        return activity.generateJson();
    }

    public Element generateHtml() {
        Element html = html();
        Element ul = ul();

        html.with(head().with(title().with(new Text("Activity"))));
        html.with(body()).with(h1().with(new Text("Activity Details")),
                ul.with(
                        li().with(new Text("Identifier : " + activity.getAid())),
                        li().with(new Text("Duration Time : " + activity.getDurationTime())),
                        li().with(new Text("Sport Id : " + activity.getSid())),
                        li().with(new Text("User Id : " + activity.getUid()))));

        if(activity.getRid() != 0) ul.with(li().with(new Text("Route Id : " + activity.getRid())));

        return html;
    }

    public Element generateHtmlWithLinks() {
        Element html = html();
        Element body = body();
        Element ul = ul();

        html.with(head().with(title().with(new Text("Activity"))));
        html.with(body);
        body.with(aa("href=\"/\"").with(new Text("Root")));
        body.with(h1().with(new Text("Activity Details")),
                ul.with(
                        li().with(new Text("Identifier : " + activity.getAid())),
                        li().with(new Text("Duration Time : " + activity.getDurationTime())),
                        li().with(new Text("Sport Id : "), aa("href=\"/sports/" + activity.getSid() + "\"")
                                .with(new Text(activity.getSid()))),
                        li().with(new Text("User Id : "), aa("href=\"/users/" + activity.getUid() + "\"")
                                .with(new Text(activity.getUid())))));

        if(activity.getRid() != 0) ul.with(li().with(new Text("Rid Id : "), aa("href=\"/routes/" + activity.getRid()
                + "\"").with(new Text(activity.getRid()))));

        return html;

    }
}
