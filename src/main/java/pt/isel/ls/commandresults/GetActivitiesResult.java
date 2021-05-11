package pt.isel.ls.commandresults;


import pt.isel.ls.Element;
import pt.isel.ls.Text;
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

    public String generateJson() {
        StringBuilder json = new StringBuilder("[");

        for (Activity activity : activities) {
            json.append("\n").append(activity.generateJson()).append(",");
        }
        json.append("\n]");

        return json.toString();
    }

    public Element generateHtml() {
        Element html = html();
        Element body = body();

        html.with(head().with(title().with(new Text("Users"))));
        html.with(body.with(h1().with(new Text("User Details"))));

        for (Activity activity : activities) {
            body.with(ul().with(
                    li().with(new Text("Identifier : " + activity.getAid())),
                    li().with(new Text("Date : " + activity.getDate())),
                    li().with(new Text("Duration Time : " + activity.getDurationTime())),
                    li().with(new Text("Sport Id : " + activity.getSid())),
                    li().with(new Text("User Id : " + activity.getUid())),
                    li().with(new Text("Route Id : " + activity.getRid()))));
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
