package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.User;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetUserByIdResult extends GetCommandResult {
    private User user;
    private Headers headers;

    public GetUserByIdResult(User user, Headers headers) {
        this.user = user;
        this.headers = headers;
    }

    @Override
    public boolean results() {
        printResults(generateResults());
        return false;
    }

    @Override
    public String generateResults() {
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
                case "text/plain" -> str = user.toString();
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
                    return user.toString();
                }
                case "application/json" -> {
                    return generateJson();
                }
                default -> {
                    return generateHtml().generateStringHtml("");
                }
            }
        }
        return "";
    }

    public String generateJson() {
        return user.generateJson();
    }

    public Element generateHtml() {
        Element html = html();
        Element body = body();

        html.with(head().with(title().with(new Text("Users"))));
        html.with(body.with(h1().with(new Text("User Details")),
                ul().with(
                        li().with(new Text("Identifier : " + user.getUid())),
                        li().with(new Text("Name : " + user.getName())),
                        li().with(new Text("Email : " + user.getEmail())))));

        return html;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
