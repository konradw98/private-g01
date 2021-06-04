package pt.isel.ls.commandresults;

public class ExitResult implements CommandResult {
    @Override
    public boolean results(boolean http) {
        System.out.println(generateResults(http));
        return true;
    }

    @Override
    public String generateResults(boolean http) {
        return "Application stopped";
    }


}
