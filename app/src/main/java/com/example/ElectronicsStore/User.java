package com.example.ElectronicsStore;

public class User {
    private String id;
    private String Email;
    private String password;
    private String firstName;
    private String secondName;
    private String number;
    Address address;

    private static User INSTANCE;

    private User() {
    }

    public static User getInstance(){
        if(INSTANCE==null){
            INSTANCE = new User();
        }
        return INSTANCE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSecondName() { return secondName; }

    public void setSecondName(String secondName) { this.secondName = secondName; }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() { return number; }

    public void setNumber(String number) { this.number = number; }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}