package pt.isel.ls.commandresults;

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
}
