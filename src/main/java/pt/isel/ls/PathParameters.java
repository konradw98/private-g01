package pt.isel.ls;

import java.util.HashMap;

public class PathParameters {
    private final HashMap<String, String> pathParameters;

    public PathParameters() {
        pathParameters = new HashMap<>();
    }

    public void addPathParameter(String name, String value) {
        pathParameters.put(name, value);
    }

    public HashMap<String, String> getPathParameters() {
        return pathParameters;
    }

    public String get(String key) {
        return pathParameters.get(key);
    }

    public void clear() {
        pathParameters.clear();
    }
}
