package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GetUsersResult extends GetCommandResult {
    private final ArrayList<User> users;
    private Headers headers;


    public GetUsersResult(ArrayList<User> users, Headers headers) {
        this.headers = headers;
        this.users = users;
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
                    for (User user : users) {
                        stringBuilder.append(user.toString());
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
                    for (User user : users) {
                        System.out.println(user);
                    }
                }
                case "application/json" -> System.out.println(generateJson());
                default -> System.out.println(generateHtml().generateStringHtml(""));
            }
        }

    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    @Override
    public boolean results() {
        generateResult(headers);
        return false;
    }

    public String generateJson() {
        StringBuilder json = new StringBuilder("[");

        for (User user : users) {
            json.append("\n").append(user.generateJson()).append(",");
        }
        json.append("\n]");

        return json.toString();
    }

    public Element generateHtml() {
        Element html = html();
        Element table = table();

        html.with(head().with(title().with(new Text("Users"))));
        html.with(body().with(table));
        table.with(h1().with(new Text("User Details")));

        table.with(tr().with(
                th().with(new Text("Identifier")),
                th().with(new Text("Name")),
                th().with(new Text("Email"))));

        for (User user : users) {
            table.with(tr().with(
                    td().with(new Text(user.getUid())),
                    td().with(new Text(user.getName())),
                    td().with(new Text(user.getEmail()))));
        }

        return html;

    }
}
