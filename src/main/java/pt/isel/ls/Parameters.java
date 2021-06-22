package pt.isel.ls;

import java.util.HashMap;

public class Parameters {
    private final HashMap<String, String> parameters;

    public Parameters(String sequence) {
        parameters = new HashMap<>();
        addParametersFromValidString(sequence);
    }

    public Parameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    private void addParametersFromValidString(String sequence) {
        if (sequence != null) {
            String[] parameters = sequence.split("&");
            if (parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    if (parameters[i].contains("=")) {
                        parameters[i] = parameters[i].replace('+', ' ');
                        parameters[i] = parameters[i].replace("%3A", ":");
                        String[] value = parameters[i].split("=");
                        if (value[0].equals("activity")) {
                            value[0] += i;
                        }
                        if (value[0].equals("duration") && value[1].length() == 5) {
                            value[1] += ":00";
                        }
                        addParameter(value[0], value[1]);
                    }
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
