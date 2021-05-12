package pt.isel.ls.commandresults;

public class PostResult implements CommandResult {
    int id;
    String label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PostResult(int id, String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public boolean results() {
        System.out.println(label + ": " + id);
        return true;
    }
}
