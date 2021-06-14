package pt.isel.ls.commandresults;

public class ListenResult implements CommandResult {
    @Override
    public boolean results(boolean http) {
        return false;
    }

    @Override
    public String generateResults(boolean http) {
        return "";
    }
}
