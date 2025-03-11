package com.confused.config;

public class CarDetails {

    private static String  registration;
    private static String  make;
    private static String  model;
    private static String  year;
    public static String getRegistration() {
        return registration;
    }
    public void setRegistration(String registration) {
        this.registration = registration;
    }
    public static String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public static String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public static String getYear() {
        return year;
    }
    public void setYear(String year) {
            this.year = year;
        }
}