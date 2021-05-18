package pt.isel.ls.commandresults;

import java.util.List;

public class DeleteResult implements CommandResult {
    private int userId;
    private List<String> activitiesId;

    @Override
    public boolean results() {
        System.out.println("user id: " + userId + " activities id:");
        for (int i = 0; i < activitiesId.size(); i++) {
            System.out.print(activitiesId.get(i));
            if (i != activitiesId.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
        return false;
    }

    public DeleteResult(int userId, List<String> activitiesId) {
        this.userId = userId;
        this.activitiesId = activitiesId;
    }
}
