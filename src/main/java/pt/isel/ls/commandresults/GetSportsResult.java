package pt.isel.ls.commandresults;

import pt.isel.ls.models.Sport;
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

    public ArrayList<Sport> getSports() {
        return sports;
    }

    public void setSports(ArrayList<Sport> sports) {
        this.sports = sports;
    }
}
