package pt.isel.ls;

import java.util.ArrayList;

public class CommandRequest {
    private ArrayList<String> parameters;

    public CommandRequest(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }
}
