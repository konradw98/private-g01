package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.Sport;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetSportResult extends GetCommandResult {
    private Sport sport;
    private Headers headers;

    public GetSportResult(Sport sport, Headers headers) {
        this.headers = headers;
        this.sport = sport;
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
                    try {
                        String str = sport.toString(); // TUTU
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                        writer.write(str);
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    System.out.println(sport);
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

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}