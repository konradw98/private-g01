package pt.isel.ls.commandresults;

public class WrongParametersResult implements CommandResult {
    String wrongParameters = "";

    public WrongParametersResult() {
    }

    public WrongParametersResult(String wrongParameters) {
        this.wrongParameters = wrongParameters;
    }

    @Override
    public boolean results(boolean http) {
        System.out.println(generateResults(http));
        return false;
    }

    @Override
    public String generateResults(boolean http) {
        return "Wrong parameters " + wrongParameters;
    }
}
