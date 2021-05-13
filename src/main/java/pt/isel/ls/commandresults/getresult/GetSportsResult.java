package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Sport;

import java.util.ArrayList;

public class GetSportsResult extends GetCommandResult {
    private ArrayList<Sport> sports;
    private Headers headers;

    public GetSportsResult(ArrayList<Sport> sports, Headers headers) {
        this.headers = headers;
        this.sports = sports;
    }

    public void generateResult(Headers headers) {
        String accept = headers.get("accept");
        String fileName = headers.get("file-name");

        if (fileName != null) {
            switch (accept) {
                case "text/plain" -> {
                    //do pliki text plain
                }
                case "application/json" -> {
                    //do pliku json
                }
                case "text/html" -> {
                    //do pliku json
                }
                default -> {
                    // do pliku html
                }
            }
        } else {
            switch (accept) {
                case "text/plain" -> {
                    for (Sport sport : sports) {
                        System.out.println(sport);
                    }
                }
                case "application/json" -> {
                    System.out.println(generateJson());
                }
                case "text/html" -> {
                    //do konsoli html
                }
                default -> {
                    // do konsoli html
                }
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

        for (Sport sport : sports) {
            json.append("\n").append(sport.generateJson()).append(",");
        }
        json.append("\n]");

        return json.toString();
    }

    public Element generateHtml() {
        Element html = html();
        Element table = table();

        html.with(head().with(title().with(new Text("Sports"))));
        html.with(body().with(table));
        table.with(h1().with(new Text("Sport Details")));

        table.with(tr().with(
                th().with(new Text("Identifier : ")),
                th().with(new Text("Name : ")),
                th().with(new Text("Description : "))));

        for (Sport sport : sports) {
            table.with(tr().with(
                    td().with(new Text(sport.getSid())),
                    td().with(new Text(sport.getName())),
                    td().with(new Text(sport.getDescription()))));
        }

        return html;

    }

    public ArrayList<Sport> getSports() {
        return sports;
    }

    public void setSports(ArrayList<Sport> sports) {
        this.sports = sports;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
