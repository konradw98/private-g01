package pt.isel.ls.commandresults;

import java.util.List;

public class DeleteResult  implements CommandResult{
    private int userId;
    private List<String> activitiesId;

    @Override
    public void print() {
        System.out.println("user id: "+userId+" activities id:");
        for (String id: activitiesId) {
            System.out.print(id+", ");
        }

    }

    public DeleteResult(int userId, List<String> activitiesId) {
        this.userId = userId;
        this.activitiesId = activitiesId;
    }
}
