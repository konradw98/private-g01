package pt.isel.ls.commandresults;

public class WrongParametersResult implements CommandResult {
    String wrongParameters = "";

    public WrongParametersResult() {
    }

    public WrongParametersResult(String wrongParameters) {
        this.wrongParameters = wrongParameters;
    }

    @Override
    public boolean results() {
        System.out.println("Wrong parameters " + wrongParameters);
        return true;
    }
}
