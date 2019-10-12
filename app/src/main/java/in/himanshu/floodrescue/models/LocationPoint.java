package in.himanshu.floodrescue.models;

import java.io.Serializable;

public class LocationPoint implements Serializable {

    private String name;
    private double lat;
    private double lon;
    private double elevation;
    private int requirement;
    private double pop;
    private double reach;


    public LocationPoint(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public LocationPoint(String name, double lat, double lon, double elevation) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.elevation = elevation;
    }

    public LocationPoint(String name, double lat, double lon, int requirement) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.requirement = requirement;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public int getRequirement() {
        return requirement;
    }

    public void setRequirement(int requirement) {
        this.requirement = requirement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }

    public double getReach() {
        return reach;
    }

    public void setReach(double reach) {
        this.reach = reach;
    }

//    @Override
//    public int compareTo(LocationPoint locationPoint) {
//
//        if (this.elevation >= locationPoint.elevation) {
//            return 1;
//        } else {
//            return -1;
//        }
//
//    }
}
