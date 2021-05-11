package pt.isel.ls;

import java.util.HashMap;

public class Headers {
    private final HashMap<String, String> headers;

    public Headers() {
        headers = new HashMap<>();
    }

    public Headers(String name, String value) {
        headers = new HashMap<>();
        headers.put(name, value);
    }

    public void addHeader(String name, String value){
        headers.put(name, value);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }
}
