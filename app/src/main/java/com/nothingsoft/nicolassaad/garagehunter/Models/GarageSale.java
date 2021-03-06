package com.nothingsoft.nicolassaad.garagehunter.Models;

/**
 * This is a model class is designed to store data in the Firebase server
 * using an object
 */
public class GarageSale {
    private String title, description, address, startDate, endDate, image1, image2, image3;
    private double lat, lon;

    public GarageSale() {
    }

    public GarageSale(String title, String description, String address, double lat, double lon, String startDate, String endDate, String image1,
                      String image2, String image3) {

        setTitle(title);
        this.description = description;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lat = lat;
        this.lon = lon;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    @Override
    public String toString() {
        return "GarageSale{title='" + title + "', description='" + description + "', address='" + address + "', lat='" + lat + "', lon='" + lon + "', startDate='" + startDate + "', endDate='" + endDate + "'}";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.equals("")) {
            throw new IllegalArgumentException("Can't be empty string");
        }
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

    public void setImage2(String image2) {this.image2 = image2;}

    public String getImage3() {return image3;}

    public void setImage3(String image3) {this.image3 = image3;}

    public String getStartDate() {return startDate;}

    public void setStartDate(String startDate) {this.startDate = startDate;}

    public String getEndDate() {return endDate;}

    public void setEndDate(String endDate) {this.endDate = endDate;}
}

