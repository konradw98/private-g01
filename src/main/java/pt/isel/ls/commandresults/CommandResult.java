package pt.isel.ls.commandresults;

public interface CommandResult {
    boolean results(boolean http);

    String generateResults(boolean http);
}
