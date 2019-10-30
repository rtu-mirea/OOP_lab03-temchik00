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
    public boolean Enter(String login, String password){
        return this.login == login && this.password == password;
    }
}
