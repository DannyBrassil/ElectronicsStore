package com.example.ElectronicsStore;

import java.util.ArrayList;

public class Store {

    private String id;
    private String email;
    private String password;
    private String name;


    public Store(){

    }

    public Store(String id, String e, String p, String name ) {
        this.id=id;
        this.email=e;
        this.password=p;
        this.name = name;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
