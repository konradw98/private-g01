package pt.isel.ls;

import java.util.HashMap;

public class Parameters {
    private final HashMap<String, String> parameters;

    public Parameters(String sequence) {
        parameters = new HashMap<>();
        addParametersFromValidString(sequence);
    }

    private void addParametersFromValidString(String sequence) {
        String[] parameters = sequence.split("&");
        for (String parameter : parameters) {
            String[] value = parameter.split("=");
            addParameter(value[0], value[1]);
        }
    }

    public void addParameter(String name, String value) {
        parameters.put(name, value);
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public String get(String key) {
        return parameters.get(key);
    }

    public int size() {
        return parameters.size();
    }
}
