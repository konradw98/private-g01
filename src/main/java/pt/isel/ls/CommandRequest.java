package pt.isel.ls;

public class CommandRequest {
    private String parameters;

    public CommandRequest(String parameters) {
        this.parameters = parameters;
    }

    public CommandRequest() {
        parameters = "";
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
