package com.example.indoor_navigation_new;

public class Users {

    public String Name, DeviceName;

    public Users(){

    }

    public String getName() {
        return Name;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public Users(String name, String deviceName) {
        Name = name;
        DeviceName = deviceName;
    }
}
