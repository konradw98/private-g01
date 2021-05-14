package pt.isel.ls.models;

public class Sport implements Model {
    private int sid;
    private String name;
    private String description;

    public Sport(int sid, String name, String description) {
        this.sid = sid;
        this.name = name;
        this.description = description;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void print() {
        System.out.println("id: " + sid + " name: " + name + " description: " + description);
    }

    @Override
    public String toString() {
        return "id: " + sid + " name: " + name + " description: " + description;
    }

    @Override
    public String generateJson() {
        return "{ \n \"id\": " + sid + ",\n \"name\": " + name + ",\n \"description\":" + description + ",\n}";
    }
}
