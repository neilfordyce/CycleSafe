package com.example.cyclesafe;

/**
 * Entity class representing a distance, point and direction between two 
 * vehicles
 * To be packaged into JSON or sending to (lorry) clients.
 * @author Calum
 */
public class Proximity 
{
//    enum DIRECTION
//    {
//       NORTH,
//       NORTH_EAST,
//       EAST,
//       SOUTH_EAST,
//       SOUTH,
//       SOUTH_WEST,
//       WEST,
//       NORTH_WEST
//    }    
    private double distance;
    private double latitude;
    private double longitude;
    private String id;
//    private DIRECTION direction;
    
    public Proximity(double distance, double lat, double lng, String id)
    {
        this.distance = distance;
        this.latitude = lat;
        this.longitude = lng;
        this.id = id;
//        this.direction = direction;
    }

    public String getId() {
    	return id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
}
