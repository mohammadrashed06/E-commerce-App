package com.example.dorra.Model;

public class AdminOrders {
    private String name,phone,city,date,state,streetPlace,buildnumber,homenumber,time,totalAmount;
    public  AdminOrders(){

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreetPlace() {
        return streetPlace;
    }

    public void setStreetPlace(String streetPlace) {
        this.streetPlace = streetPlace;
    }

    public String getBuildnumber() {
        return buildnumber;
    }

    public void setBuildnumber(String buildnumber) {
        this.buildnumber = buildnumber;
    }

    public String getHomenumber() {
        return homenumber;
    }

    public void setHomenumber(String homenumber) {
        this.homenumber = homenumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public AdminOrders(String name, String phone, String city, String streetPlace, String buildnumber, String homenumber, String totalAmount, String state) {
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.streetPlace = streetPlace;
        this.buildnumber = buildnumber;
        this.homenumber = homenumber;
        this.totalAmount = totalAmount;
        this.state = state;

    }
}
