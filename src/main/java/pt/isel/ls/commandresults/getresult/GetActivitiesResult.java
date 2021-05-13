package pt.isel.ls.commandresults.getresult;


import pt.isel.ls.Element;
import pt.isel.ls.Text;
import pt.isel.ls.models.Activity;
import java.util.ArrayList;

public class GetActivitiesResult extends GetCommandResult {
    private ArrayList<Activity> activities;

    public GetActivitiesResult(ArrayList<Activity> activities) {
        ///TODO ADD HEADERS
        this.activities = activities;
    }

    @Override
    public boolean results() {
        for (Activity activity : activities) {
            activity.print();
        }
        return false;
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
        Element table = table();

        html.with(head().with(title().with(new Text("Activities"))));
        html.with(body().with(table));
        table.with(h1().with(new Text("Activity Details")));
        table.with(tr().with(
                th().with(new Text("Identifier : ")),
                th().with(new Text("Date : ")),
                th().with(new Text("Duration Time : ")),
                th().with(new Text("Sport Id : ")),
                th().with(new Text("User Id : ")),
                th().with(new Text("Route Id : "))));

        for (Activity activity : activities) {
            table.with(tr().with(
                    td().with(new Text(activity.getAid())),
                    td().with(new Text(activity.getDate())),
                    td().with(new Text(activity.getDurationTime())),
                    td().with(new Text(activity.getSid())),
                    td().with(new Text(activity.getUid())),
                    td().with(new Text(activity.getRid()))));
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
