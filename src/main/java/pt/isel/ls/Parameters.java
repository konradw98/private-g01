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
        if (parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].contains("=")) {
                    parameters[i] = parameters[i].replace('+', ' ');
                    String[] value = parameters[i].split("=");
                    if (value[0].equals("activity")) {
                        value[0] += i;
                    }
                    addParameter(value[0], value[1]);
                }
            }
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
