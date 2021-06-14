package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.getresult.GetRootResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Root;

public class GetRootHandler extends GetHandler implements CommandHandler {
    @Override
    public CommandResult execute(CommandRequest commandRequest) throws Exception {
        return new GetRootResult(new Root(), commandRequest.getHeaders());
    }
}
