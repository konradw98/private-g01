package pt.isel.ls.commandresults;

public class EmptyTableResult implements CommandResult {

    String emptyTable;

    public EmptyTableResult(String emptyTable) {
        this.emptyTable = emptyTable;
    }

    @Override
    public boolean results(boolean http) {
        System.out.println(generateResults(http));
        return false;
    }

    @Override
    public String generateResults(boolean http) {
        return "Empty " + emptyTable + " table!";
    }
}

