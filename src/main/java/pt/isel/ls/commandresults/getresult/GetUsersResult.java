package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.Text;
import pt.isel.ls.models.User;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GetUsersResult extends GetCommandResult {
    private final ArrayList<User> users;
    private Headers headers;
    private int skip;
    private int top;


    public GetUsersResult(ArrayList<User> users, Headers headers, Parameters parameters) {
        this.headers = headers;
        this.users = users;
        this.skip = Integer.parseInt(parameters.get("skip"));
        this.top = Integer.parseInt(parameters.get("top"));
    }

    public void printResults(String result) {
        if (!result.equals("")) {
            System.out.println(result);
        }
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
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
                    StringBuilder stringBuilder = new StringBuilder();
                    for (User user : users) {
                        stringBuilder.append(user.toString());
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
                    for (User user : users) {
                        stringBuilder.append(user.toString());
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

        for (User user : users) {
            json.append("\n").append(user.generateJson()).append(",");
        }
        json.deleteCharAt(json.length() - 1);
        json.append("\n]");

        return json.toString();
    }

    public Element generateHtml() {
        Element html = html();
        Element table = table("border=1");

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

    public Element generateHtmlWithLinks() {
        int pageNumber = getPageNumber(skip, top);
        //not valid
        if (pageNumber == -1) {
            return null;
        }

        Element html = html();
        Element body = body();
        Element table = table("border=1");
        Element form = form("action=\"/users\" method=\"POST\"");
        form.with(div().with(label(" for=\"name\"").with(new Text("name")),input("type=\"text\" "
                + "name=\"name\" id=\"name\"")));
        form.with(div().with(label(" for=\"email\"").with(new Text("email")),input("type=\"email\" "
                + "name=\"email\" id=\"email\"")));
        form.with(div().with(button("type=\"submit\"").with(new Text("Submit"))));

        html.with(head().with(title().with(new Text("Users"))));
        body.with(aa("href=\"/\"").with(new Text("Root")));
        html.with(body.with(table));
        table.with(h1().with(new Text("Users Page " + pageNumber)));

        table.with(tr().with(
                th().with(new Text("Identifier")),
                th().with(new Text("Name"))));

        for (User user : users) {
            table.with(tr().with(
                    td().with(aa("href=\"/users/" + user.getUid() + "\"").with(new Text(user.getUid()))),
                    td().with(new Text(user.getName()))));
        }

        if (users.size() == 5) {
            body.with(aa("href=\"/users?top=5&skip=" + (skip + 5) + "\"").with(new Text("Next")));
        }
        if (skip >= 5) {
            body.with(aa("href=\"/users?top=5&skip=" + (skip - 5) + "\"").with(new Text("Previous")));
        }

        body.with(form);

        return html;

    }
}
