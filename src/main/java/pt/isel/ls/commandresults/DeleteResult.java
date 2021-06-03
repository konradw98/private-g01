package pt.isel.ls.commandresults;

import java.util.List;

public class DeleteResult implements CommandResult {
    private int userId;
    private List<String> activitiesId;

    @Override
    public boolean results() {
        System.out.println(generateResults());
        return false;
    }

    @Override
    public String generateResults() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("user id: ").append(userId).append(" activities id:");
        for (int i = 0; i < activitiesId.size(); i++) {
            stringBuilder.append(activitiesId.get(i));
            if (i != activitiesId.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.append("\n").toString();
    }

    public DeleteResult(int userId, List<String> activitiesId) {
        this.userId = userId;
        this.activitiesId = activitiesId;
    }
}
