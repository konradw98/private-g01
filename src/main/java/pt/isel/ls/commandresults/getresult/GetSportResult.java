package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Sport;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetSportResult extends GetCommandResult {
    private final Sport sport;
    private Headers headers;

    public GetSportResult(Sport sport, Headers headers) {
        this.headers = headers;
        this.sport = sport;
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
                case "text/plain" -> str = sport.toString();
                case "application/json" -> str = sport.generateJson();
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
                    return sport.toString();
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
        return sport.generateJson();
    }

    public Element generateHtml() {
        Element html = html();

        html.with(head().with(title().with(new Text("Sports"))));
        html.with(body().with(h1().with(new Text("Sport Details"))),
                ul().with(
                        li().with(new Text("Identifier : " + sport.getSid())),
                        li().with(new Text("Name : " + sport.getName())),
                        li().with(new Text("Description : " + sport.getDescription()))));


        return html;
    }

    public Element generateHtmlWithLinks() {
        Element html = html();
        Element body = body();

        html.with(head().with(title().with(new Text("User"))));
        html.with(body);
        body.with(aa("href=\"/\"").with(new Text("Root")));
        body.with(aa("href=\"/sports\"").with(new Text("Sports")));
        body.with(h1().with(new Text("Sport Details")),
                ul().with(
                        li().with(new Text("Identifier : " + sport.getSid())),
                        li().with(new Text("Name : " + sport.getName())),
                        li().with(new Text("Description : " + sport.getDescription()))));
        body.with(aa("href=\"/sports/" + sport.getSid() + "/activities\"").with(new Text("Activities")));

        return html;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
