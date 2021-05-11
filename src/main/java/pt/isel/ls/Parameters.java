package pt.isel.ls;

import java.util.HashMap;

public class Parameters {
    private final HashMap<String, String> parameters;

    public Parameters(String sequence) {
        parameters = new HashMap<>();
    }

    public void addParameter(String name, String value){
        parameters.put(name, value);
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }
}
