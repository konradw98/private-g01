package pt.isel.ls.commandresults;

import pt.isel.ls.models.Sport;

public class GetSportResult implements CommandResult {
    private Sport sport;

    public GetSportResult(Sport sport) {
        this.sport = sport;
    }

    @Override
    public void print() {
        sport.print();
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
}
