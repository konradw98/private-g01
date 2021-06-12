package pt.isel.ls.models;

import java.util.ArrayList;

public class Route implements Model {
    private int rid;
    private String startLocation;
    private String endLocation;
    private double distance;
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<Sport> sports = new ArrayList<>();

    public Route(int rid, String startLocation, String endLocation, double distance) {
        this.rid = rid;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
    }

    public Route(int rid, String startLocation, String endLocation, double distance, ArrayList<Activity> activities,
                 ArrayList<Sport> sports) {
        this.rid = rid;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
        this.activities = activities;
        this.sports = sports;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public void print() {
        System.out.println("rid: " + rid + " start location: " + startLocation + " end location: " + endLocation);

    }

    @Override
    public String toString() {
        return " rid: " + rid + " start location: " + startLocation + " end location: " + endLocation;
    }

    @Override
    public String generateJson() {
        return "{ \n \"id\": " + rid + ",\n \"start location\": \"" + startLocation + "\",\n \"end location\": \""
                + endLocation + "\",\n \"distance\": " + distance + "\n}";
    }
}
