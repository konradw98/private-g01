package pt.isel.ls.commandresults;

import pt.isel.ls.handlers.ListenHandler;

public class ExitResult implements CommandResult {
    @Override
    public boolean results(boolean http) {
        log.info("Application stopped");
        return true;
    }

    @Override
    public String generateResults(boolean http) {
        try {
            ListenHandler.ListenThread.stop();
        } catch (Exception e) {
            //the server was not started
        } return null;
    }
}
