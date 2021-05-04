package pt.isel.ls.CommandResults;


import pt.isel.ls.Models.Activity;

import java.util.ArrayList;

public class GetActivitiesResult  implements  CommandResult{
    private ArrayList<Activity> activities;

    public GetActivitiesResult(ArrayList<Activity> activities) {
        this.activities=activities;
    }

    @Override
    public void print() {
        for (Activity activity : activities) {
            activity.print();
        }
    }
}
