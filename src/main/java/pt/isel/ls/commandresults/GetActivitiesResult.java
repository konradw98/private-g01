package pt.isel.ls.commandresults;


import pt.isel.ls.Element;
import pt.isel.ls.Text;
import pt.isel.ls.models.Activity;
import pt.isel.ls.models.Route;

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

    public String generateJson() {
        StringBuilder json = new StringBuilder("[");

        for (Activity activity : activities) {
            json.append("\n").append(activity.generateJSON()).append(",");
        }
        json.append("\n]");

        return json.toString();
    }

    public Element generateHtml() {
        Element html = html();
        Element body = body();

        html.with(head().with(title().with(new Text("Users"))), head());
        html.with(body.with(h1().with(new Text("User Details")),
                h1()),
                body());

        for(Activity activity: activities)
        {
            body.with(ul().with(
                    li().with(new Text("Identifier : " + activity.getAid())),li(),
                    li().with(new Text("Date : " + activity.getDate())),li(),
                    li().with(new Text("Duration Time : " + activity.getDurationTime())),li(),
                    li().with(new Text("Sport Id : " + activity.getSid())),li(),
                    li().with(new Text("User Id : " + activity.getUid())),li(),
                    li().with(new Text("Route Id : " + activity.getRid())),li()),
                    ul());
        }

        return html;

    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}
