package pt.isel.ls.commandresults;

public class ExitResult implements CommandResult {
    @Override
    public boolean results() {
        System.out.println(generateResults());
        return true;
    }

    @Override
    public String generateResults() {
        return "Application stopped";
    }


}
