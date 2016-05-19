package com.nothingsoft.nicolassaad.garagehunter;

/**
 * This is a model class is designed to store data in the Firebase server
 * using an object
 */
public class GarageSale {
    private String title, description, address, weekday, image1, image2, image3;
    private double lat, lon;

    public GarageSale() {

    }

    public GarageSale(String title, String description, String address, double lat, double lon, String weekday, String image1,
                      String image2, String image3) {

        this.title = title;
        this.description = description;
        this.address = address;
        this.weekday = weekday;
        this.lat = lat;
        this.lon = lon;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
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

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}

