package pt.isel.ls.models;

public class User implements Model {
    private int uid;
    private String email;
    private String name;


    public User(int uid, String email, String name) {
        this.uid = uid;
        this.email = email;
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void print() {
        System.out.println("id: " + uid + " email: " + email + " name: " + name);
    }

    @Override
    public String toString() {
        return "User{"
                + "uid=" + uid
                + ", email=" + email
                + ", name=" + name
                + '}';
    }
}
