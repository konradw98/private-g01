package pt.isel.ls.Models;

public class Route implements Model {
    private int rid;
    private String start_location;
    private String end_location;
    private double distance;

    public Route(int rid, String start_location, String end_location, double distance) {
        this.rid = rid;
        this.start_location = start_location;
        this.end_location = end_location;
        this.distance = distance;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getStart_location() {
        return start_location;
    }

    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    public String getEnd_location() {
        return end_location;
    }

    public void setEnd_location(String end_location) {
        this.end_location = end_location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public void print() {
        System.out.println("rid: "+rid+" start location: "+start_location+" end location: "+end_location);

    }
}
