package pt.isel.ls.commandresults;


import pt.isel.ls.models.Activity;
import java.util.ArrayList;

public class GetActivitiesResult implements CommandResult {
    private ArrayList<Activity> activities;

    public GetActivitiesResult(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public void print() {
        for (Activity activity : activities) {
            activity.print();
        }
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}
