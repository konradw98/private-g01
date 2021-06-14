package pt.isel.ls.models;

import java.util.ArrayList;

public class Route implements Model {
    private int rid;
    private final String startLocation;
    private final String endLocation;
    private final double distance;

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

    public String getEndLocation() {
        return endLocation;
    }

    public double getDistance() {
        return distance;
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
