package com.company;

public class User {
    protected String name;
    protected String login;
    protected String password;
    public User(String name, String login, String password){
        this.name = name;
        this.login = login;
        this.password = password;
    }
    public boolean enter(String login, String password){
        return this.login.compareTo(login) == 0 && this.password.compareTo(password) == 0;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
