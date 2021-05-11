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
    public void print() {
        for (User user : users) {
            user.print();
        }
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
        Element body = body();

        html.with(head().with(title().with(new Text("Users"))));
        html.with(body.with(h1().with(new Text("User Details"))));

        for(User user: users)
        {
            body.with(ul().with(
                    li().with(new Text("Identifier : " + user.getUid())),
                    li().with(new Text("Name : " + user.getName())),
                    li().with(new Text("Email : " + user.getEmail()))));
        }

        return html;

    }
}
