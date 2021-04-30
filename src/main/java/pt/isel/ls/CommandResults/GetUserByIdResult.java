package pt.isel.ls.CommandResults;

import pt.isel.ls.Models.User;

public class GetUserByIdResult implements CommandResult {
    private User user;

    public GetUserByIdResult(User user) {
        this.user = user;
    }

    @Override
    public void print() {
        user.print();
    }

}
