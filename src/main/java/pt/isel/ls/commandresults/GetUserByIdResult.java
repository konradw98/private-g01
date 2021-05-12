package pt.isel.ls.commandresults;

import pt.isel.ls.models.User;

public class GetUserByIdResult implements CommandResult {
    private User user;

    public GetUserByIdResult(User user) {
        this.user = user;
    }

    @Override
    public boolean results() {
        user.print();
        return true;
    }


    public String generateJson() {
        return user.generateJson();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
