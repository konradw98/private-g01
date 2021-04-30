package pt.isel.ls.CommandResults;

public class PostResult implements CommandResult {
    int id;
    String label;

    public PostResult(int id, String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public void print() {
        System.out.println(label + ": " + id);
    }
}
