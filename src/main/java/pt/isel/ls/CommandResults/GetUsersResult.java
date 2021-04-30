package pt.isel.ls.CommandResults;

import pt.isel.ls.Models.User;

import java.util.ArrayList;

public class GetUsersResult implements CommandResult {
    private ArrayList<User> users;

    public GetUsersResult(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public void print() {
        for (User user : users) {
            user.print();
        }
    }
}
