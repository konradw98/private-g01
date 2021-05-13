package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.Headers;
import pt.isel.ls.Text;
import pt.isel.ls.models.User;

public class GetUserByIdResult extends  GetCommandResult{
    private User user;

    public GetUserByIdResult(User user, Headers headers) {
        super(headers);
        this.user = user;
    }

    @Override
    public boolean results() {
        user.print();
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
}
