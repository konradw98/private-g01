package pt.isel.ls.models;

public class Route implements Model {
    private int rid;
    private String startLocation;
    private String endLocation;
    private double distance;

    public Route(int rid, String startLocation, String endLocation, double distance) {
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
        return "Route{"
                + "rid=" + rid
                + ", startLocation='" + startLocation
                + ", endLocation='" + endLocation
                + ", distance=" + distance
                + '}';
    }
}
