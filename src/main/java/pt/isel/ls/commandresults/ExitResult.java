package pt.isel.ls.commandresults;

import pt.isel.ls.handlers.ListenHandler;

public class ExitResult implements CommandResult {
    @Override
    public boolean results(boolean http) {
        System.out.println(generateResults(http));
        return true;
    }

    @Override
    public String generateResults(boolean http) {
        try {
            ListenHandler.ListenThread.stop();
        } catch (Exception e) {
            //the server was not started
        }
        return "Application stopped";
    }
}
