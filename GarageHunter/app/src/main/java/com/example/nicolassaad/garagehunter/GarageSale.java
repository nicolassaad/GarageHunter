package com.example.nicolassaad.garagehunter;

/**
 * Created by nicolassaad on 5/6/16.
 */
public class GarageSale {
    private String title, description, address, weekday;
    private int lat, lon;

    public GarageSale() {

    }

    public GarageSale(String title, String description, String address, int lat, int lon, String weekday) {
<<<<<<< HEAD

=======
>>>>>>> 8ebcdd2b433bf0039a40e0240aa7a0e8cf01feda
        this.title = title;
        this.description = description;
        this.address = address;
        this.weekday = weekday;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "GarageSale{title='" + title + "', description='" + description + "', address='" + address + "', lat='" + lat + "', lon='" + lon + "', weekday='" + weekday + "'}";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }
}
<<<<<<< HEAD
=======

>>>>>>> 8ebcdd2b433bf0039a40e0240aa7a0e8cf01feda
