package pt.isel.ls.commandresults;

import java.util.List;

public class DeleteResult implements CommandResult {
    private int userId;
    private List<String> activitiesId;

    @Override
    public boolean results() {
        System.out.println("user id: " + userId + " activities id:");
        for (String id : activitiesId) {
            System.out.print(id + ", ");
        }
        return false;
    }

    public DeleteResult(int userId, List<String> activitiesId) {
        this.userId = userId;
        this.activitiesId = activitiesId;
    }
}
