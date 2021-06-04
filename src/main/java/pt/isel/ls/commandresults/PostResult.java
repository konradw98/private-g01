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
    public boolean results(boolean http) {
        System.out.println(generateResults(http));
        return false;
    }

    @Override
    public String generateResults(boolean http) {
        return label + ": " + id;
    }
}
