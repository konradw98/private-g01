package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.Text;
import pt.isel.ls.models.Sport;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GetSportsResult extends GetCommandResult {
    private ArrayList<Sport> sports;
    private Headers headers;
    private int skip;
    private int top;

    public GetSportsResult(ArrayList<Sport> sports, Headers headers, Parameters parameters) {
        this.headers = headers;
        this.sports = sports;
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
                    for (Sport sport : sports) {
                        stringBuilder.append(sport.toString());
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
                    for (Sport sport : sports) {
                        stringBuilder.append(sport);
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

        for (Sport sport : sports) {
            json.append("\n").append(sport.generateJson()).append(",");
        }
        json.deleteCharAt(json.length() - 1);
        json.append("\n]");

        return json.toString();
    }

    public Element generateHtml() {
        Element html = html();
        Element table = table("border=1");

        html.with(head().with(title().with(new Text("Sports"))));
        html.with(body().with(table));
        table.with(h1().with(new Text("Sport Details")));

        table.with(tr().with(
                th().with(new Text("Identifier")),
                th().with(new Text("Name")),
                th().with(new Text("Description"))));

        for (Sport sport : sports) {
            table.with(tr().with(
                    td().with(new Text(sport.getSid())),
                    td().with(new Text(sport.getName())),
                    td().with(new Text(sport.getDescription()))));
        }

        return html;

    }

    public Element generateHtmlWithLinks() {
        int pageNumber = getPageNumber(skip, top);
        //not valid
        if (pageNumber == -1) {
            return null;
        }

        Element html = html();
        html.with(head().with(title().with(new Text("Sports"))));

        Element body = body();
        body.with(aa("href=\"/\"").with(new Text("Root")));

        Element table = table("border=1");
        html.with(body.with(table));
        table.with(h1().with(new Text("Sports Page " + pageNumber)));

        table.with(tr().with(
                th().with(new Text("Identifier")),
                th().with(new Text("Name"))));

        for (Sport sport : sports) {
            table.with(tr().with(
                    td().with(aa("href=\"/sports/" + sport.getSid() + "\"").with(new Text(sport.getSid()))),
                    td().with(new Text(sport.getName()))));
        }

        if (sports.size() == 5) {
            body.with(aa("href=\"/sports?top=5&skip=" + (skip + 5) + "\"").with(new Text("Next")));
        }
        if (skip >= 5) {
            body.with(aa("href=\"/sports?top=5&skip=" + (skip - 5) + "\"").with(new Text("Previous")));
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
