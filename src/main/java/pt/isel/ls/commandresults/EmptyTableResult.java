package pt.isel.ls.commandresults;

public class EmptyTableResult implements CommandResult {

    String emptyTable;

    public EmptyTableResult(String emptyTable) {
        this.emptyTable = emptyTable;
    }

    @Override
    public boolean results() {
        System.out.println("Empty " + emptyTable + " table!");
        return false;
    }
}

