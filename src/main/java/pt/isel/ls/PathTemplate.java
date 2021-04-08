package pt.isel.ls;

public class PathTemplate<T> {
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
}
