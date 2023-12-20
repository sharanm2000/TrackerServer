package com.ambulance.tracker;

public class Ambulance {
    public String ambulance_name;
    public String registration_number;
    public String ambulance_model;
    public String city;
    public String misc;

    public String getAmbulance_name() {
        return ambulance_name;
    }

    public void setAmbulance_name(String ambulance_name) {
        this.ambulance_name = ambulance_name;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getAmbulance_model() {
        return ambulance_model;
    }

    public void setAmbulance_model(String ambulance_model) {
        this.ambulance_model = ambulance_model;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }
}
