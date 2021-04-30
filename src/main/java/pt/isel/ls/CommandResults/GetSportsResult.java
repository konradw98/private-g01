package pt.isel.ls.CommandResults;

import pt.isel.ls.Models.Sport;
import pt.isel.ls.Models.User;

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
}
