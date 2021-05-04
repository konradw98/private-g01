package pt.isel.ls.CommandResults;

public class WrongParametersResult implements CommandResult {
    String wrongParameters = "";

    public WrongParametersResult() {
    }

    public WrongParametersResult(String wrongParameters) {
        this.wrongParameters = wrongParameters;
    }

    @Override
    public void print() {
        System.out.println("Wrong parameters "+wrongParameters);
    }
}
