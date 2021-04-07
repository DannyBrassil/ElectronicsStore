package com.example.ElectronicsStore;

public class User {
    private String Email;
    private String password;
    private String firstName;
    private String secondName;
    private String number;
    Address address;


    public User() { }

    public User(String email, String password, String fname, String num, Address address) {
        this.Email = email; this.password = password; this.firstName = fname;  this.number=num; this.address=address;
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