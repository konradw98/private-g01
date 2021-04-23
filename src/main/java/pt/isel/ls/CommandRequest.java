package pt.isel.ls;

import java.util.ArrayList;

public class CommandRequest {
    private ArrayList<String> pathParameters;
    private ArrayList<String> parameters;

    public CommandRequest(ArrayList<String> pathParameters, ArrayList<String> parameters) {
        this.pathParameters = pathParameters;
        this.parameters = parameters;
    }

    public CommandRequest(ArrayList<String> pathParameters) {
        this.pathParameters = pathParameters;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public ArrayList<String> getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(ArrayList<String> parameters) {
        this.pathParameters = parameters;
    }
}
