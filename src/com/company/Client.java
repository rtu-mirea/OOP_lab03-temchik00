package com.company;

public class Client extends User{
    private String place;
    public Client(String name, String login, String password, String place) {
        super(name, login, password);
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
