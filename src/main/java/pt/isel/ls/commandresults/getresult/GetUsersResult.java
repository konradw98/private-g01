package pt.isel.ls.commandresults.getresult;


import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.User;

import java.util.ArrayList;

public class GetUsersResult extends GetCommandResult {
    private ArrayList<User> users;
    private Headers headers;


    public GetUsersResult(ArrayList<User> users, Headers headers) {
        this.headers = headers;
        this.users = users;
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
                    for (User user : users) {
                        System.out.println(user);
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

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
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
                th().with(new Text("Identifier : ")),
                th().with(new Text("Name : ")),
                th().with(new Text("Email : "))));

        for (User user : users) {
            table.with(tr().with(
                    td().with(new Text(user.getUid())),
                    td().with(new Text(user.getName())),
                    td().with(new Text(user.getEmail()))));
        }

        return html;

    }
}
