package pt.isel.ls.commandresults;

public class ExitResult implements CommandResult {
    @Override
    public boolean results() {
        System.out.println("Application stopped");
        return true;
    }


}
