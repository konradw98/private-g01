package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.models.Route;
import pt.isel.ls.models.Sport;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetActivitiesNumberBySidResult extends GetCommandResult implements CommandResult {

    private final Sport sport;
    private Headers headers;
    private final int number;

    public GetActivitiesNumberBySidResult (Sport sport, int number, Headers headers) {
        this.sport = sport;
        this.headers = headers;
        this.number = number;
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
                case "text/plain" -> str = generateTextPlain();
                case "application/json" -> str = generateJson();
                default -> str = generateHtml().generateStringHtml("");
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(str);
                writer.close();
            } catch (Exception e) {
                System.out.println("Couldn't write to given file");
            }
        } else {
            switch (accept) {
                case "text/plain" -> System.out.println(generateTextPlain());
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
        return "{ \n \"name\": \"" + sport.getName() + "\",\n \"description\": \"" + sport.getDescription() +
                "\",\n \"amount activities\":"  + number + "\n}";
    }

    public String generateTextPlain() {
        return "name: " +sport.getName()+ " description "+sport.getDescription() + " amount activities: " + number;
    }

    public Element generateHtml() {
        Element html = html();

        html.with(head().with(title().with(new Text("Sport"))));
        html.with(body().with(h1().with(new Text("Sport Details")),
                ul().with(
                        li().with(new Text("name : " + sport.getName())),
                        li().with(new Text("description : " + sport.getDescription()))
                ),
                h1().with(new Text("activity")),
                ul().with(
                        li().with(new Text("amount of activity for this sport : " + number)))
                )
        );
        return html;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}