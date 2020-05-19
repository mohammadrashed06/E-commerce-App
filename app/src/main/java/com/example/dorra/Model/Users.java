package com.example.dorra.Model;

public class Users {
    //this class contains all data off the users frome database
    private String name,phone,password,image,city,street,build,home;
    public Users(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public Users(String name, String phone, String password, String image, String city, String street, String build, String home) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.city = city;
        this.street = street;
        this.build = build;
        this.home = home;
    }
}
