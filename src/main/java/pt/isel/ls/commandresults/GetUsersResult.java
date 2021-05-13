package pt.isel.ls.commandresults;


import pt.isel.ls.Element;
import pt.isel.ls.Text;
import pt.isel.ls.models.User;
import java.util.ArrayList;

public class GetUsersResult implements CommandResult {
    private ArrayList<User> users;

    public GetUsersResult(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public boolean results() {
        for (User user : users) {
            user.print();
        }
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
