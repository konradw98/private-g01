package pt.isel.ls.commandresults;

import pt.isel.ls.Element;
import pt.isel.ls.Text;
import pt.isel.ls.models.Sport;
import java.util.ArrayList;

public class GetSportsResult implements CommandResult {
    private ArrayList<Sport> sports;

    public GetSportsResult(ArrayList<Sport> sports) {
        this.sports = sports;
    }

    @Override
    public boolean results() {
        for (Sport sport : sports) {
            sport.print();
        }
        return false;
    }

    public String generateJson() {
        StringBuilder json = new StringBuilder("[");

        for (Sport sport : sports) {
            json.append("\n").append(sport.generateJson()).append(",");
        }
        json.append("\n]");

        return json.toString();
    }

    public Element generateHtml() {
        Element html = html();
        Element table = table();

        html.with(head().with(title().with(new Text("Sports"))));
        html.with(body().with(table));
        table.with(h1().with(new Text("Sport Details")));

        table.with(tr().with(
                th().with(new Text("Identifier : ")),
                th().with(new Text("Name : ")),
                th().with(new Text("Description : "))));

        for (Sport sport : sports) {
            table.with(tr().with(
                    td().with(new Text(sport.getSid())),
                    td().with(new Text(sport.getName())),
                    td().with(new Text(sport.getDescription()))));
        }

        return html;

    }

    public ArrayList<Sport> getSports() {
        return sports;
    }

    public void setSports(ArrayList<Sport> sports) {
        this.sports = sports;
    }

}
