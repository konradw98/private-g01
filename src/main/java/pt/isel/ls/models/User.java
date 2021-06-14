package pt.isel.ls.models;

import java.util.ArrayList;

public class User implements Model {
    private int uid;
    private String email;
    private String name;
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<Sport> sports = new ArrayList<>();

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public ArrayList<Sport> getSports() {
        return sports;
    }

    public User(int uid, String email, String name) {
        this.uid = uid;
        this.email = email;
        this.name = name;
    }

    public User(int uid, String email, String name, ArrayList<Activity> activities, ArrayList<Sport> sports) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.activities = activities;
        this.sports = sports;
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
        return "id: " + uid + " email: " + email + " name: " + name;
    }

    @Override
    public String generateJson() {
        return "{ \n \"id\": " + uid + ",\n \"name\": \"" + name + "\",\n \"email\": \"" + email + "\"\n}";
    }
}
