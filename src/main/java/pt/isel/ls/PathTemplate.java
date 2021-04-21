package pt.isel.ls;

import java.util.Arrays;
import java.util.List;

public class PathTemplate {
    private String path;

    public PathTemplate(String path) {
        this.path = path;
    }

    public String getPathTemplate() {
        return path;
    }

    public void setPathTemplate(String path) {
        this.path = path;
    }

    public List<String> splitSegmentsFromPathTemplate() {
        return Arrays.asList(path.substring(1).split("/"));
    }
}
