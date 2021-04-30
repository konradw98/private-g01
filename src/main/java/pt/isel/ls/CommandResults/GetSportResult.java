package pt.isel.ls.CommandResults;

import pt.isel.ls.Models.Sport;

public class GetSportResult implements CommandResult{
    private Sport sport;

    public GetSportResult(Sport sport) {
        this.sport = sport;
    }

    @Override
    public void print() {
        sport.print();
    }
}
