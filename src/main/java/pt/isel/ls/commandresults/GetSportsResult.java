package pt.isel.ls.commandresults;

import pt.isel.ls.models.Sport;
import pt.isel.ls.models.User;

import java.util.ArrayList;

public class GetSportsResult implements CommandResult {
    private ArrayList<Sport> sports;

    public GetSportsResult(ArrayList<Sport> sports) {
        this.sports = sports;
    }

    @Override
    public void print() {
        for (Sport sport : sports) {
            sport.print();
        }
    }

    public String generateJSON() {
        String json = "[";

        for (Sport sport : sports) {
            json = json + "\n" + sport.generateJSON() + ",";
        }
        json = json + "\n]";

        return json;
    }

    public ArrayList<Sport> getSports() {
        return sports;
    }

    public void setSports(ArrayList<Sport> sports) {
        this.sports = sports;
    }

}
