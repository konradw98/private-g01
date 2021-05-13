package pt.isel.ls;

import java.util.HashMap;

public class Headers {
    private final HashMap<String, String> headers;

    public Headers(String line) {
        headers = new HashMap<>();
        addHeadersFromString(line);
    }

    private void addHeadersFromString(String sequence) {
        //????
        String[] parameters = sequence.split("|");
        for (String parameter : parameters) {
            String[] value = parameter.split(":");
            addHeader(value[0], value[1]);
        }
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public String get(String key) {
        return headers.get(key);
    }
}
