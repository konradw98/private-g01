package pt.isel.ls.CommandResults;

public class WrongParametersResult implements CommandResult {
    public WrongParametersResult() {
    }

    @Override
    public void print() {
        System.out.println("Wrong parameters!");
    }
}
