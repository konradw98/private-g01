package pt.isel.ls.commandresults;

public class ListenResult implements CommandResult{
    @Override
    public boolean results() {
        return true;
    }

    @Override
    public String generateResults() {
        return "";
    }
}
