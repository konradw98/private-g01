package pt.isel.ls;

import java.util.Arrays;
import java.util.List;

public class Path {
    private String path;

    public Path(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> splitSegmentsFromPath() {
        return Arrays.asList(path.substring(1).split("/"));
    }
}
