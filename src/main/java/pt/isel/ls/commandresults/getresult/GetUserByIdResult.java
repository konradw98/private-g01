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

    public void generateResult(Headers headers) {
        String accept = headers.get("accept");
        String fileName = headers.get("file-name");

        if (fileName != null) {
            switch (accept) {
                case "text/plain" -> {
                    try {
                        String str = user.toString();
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
                    System.out.println(user);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
