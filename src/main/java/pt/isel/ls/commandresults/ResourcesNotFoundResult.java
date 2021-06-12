package pt.isel.ls.commandresults;

public class ResourcesNotFoundResult  implements CommandResult{
    public ResourcesNotFoundResult() {

    }

    @Override
    public boolean results(boolean http) {
        return false;
    }

    @Override
    public String generateResults(boolean http) {
        return "Resource not found";
    }
}
