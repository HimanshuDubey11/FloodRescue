package in.himanshu.floodrescue.models;

public class LocationModel {

    private double elevation;
    private double longitude;
    private double latitude;

    public LocationModel(double elevation, double longitude, double latitude) {
        this.elevation = elevation;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
