package pt.isel.ls.commandresults;

import pt.isel.ls.models.User;

public class GetUserByIdResult implements CommandResult {
    private User user;

    public GetUserByIdResult(User user) {
        this.user = user;
    }

    @Override
    public void print() {
        user.print();
    }


    public String generateJSON() {
        return user.generateJson();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
