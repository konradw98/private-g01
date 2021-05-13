package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Sport;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GetSportsResult extends GetCommandResult {
    private ArrayList<Sport> sports;
    private Headers headers;

    public GetSportsResult(ArrayList<Sport> sports, Headers headers) {
        this.headers = headers;
        this.sports = sports;
    }

    public void generateResult(Headers headers) {
        String accept;
        String fileName;
        if (headers == null) {
            accept = "text/html";
            fileName = null;
        } else {
            accept = headers.get("accept");
            fileName = headers.get("file-name");
        }

        if (fileName != null) {
            switch (accept) {
                case "text/plain" -> {
                    try {
                        StringBuilder stringBuilder = new StringBuilder("");
                        for (Sport sport : sports) {
                            stringBuilder.append(sport.toString());
                        }
                        String str = stringBuilder.toString();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                        writer.write(str);
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "application/json" -> {
                    try {
                        String str = generateJson();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                        writer.write(str);
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                default -> {
                    try {
                        String str = generateHtml().generateStringHtml("");
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                        writer.write(str);
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            switch (accept) {
                case "text/plain" -> {
                    for (Sport sport : sports) {
                        System.out.println(sport);
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
