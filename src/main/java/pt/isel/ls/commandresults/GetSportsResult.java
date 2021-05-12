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
        return true;
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
        Element body = body();

        html.with(head().with(title().with(new Text("Sports"))));
        html.with(body.with(h1().with(new Text("Sport Details"))));

        for (Sport sport : sports) {
            body.with(ul().with(
                    li().with(new Text("Identifier : " + sport.getSid())),
                    li().with(new Text("Name : " + sport.getName())),
                    li().with(new Text("Description : " + sport.getDescription()))));
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
