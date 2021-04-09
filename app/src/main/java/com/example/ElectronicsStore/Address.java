package com.example.ElectronicsStore;

public class Address {
    String line1, line2, line3, county;

    public Address(){

    }

    public Address(String line1, String line2, String line3, String county) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.county = county;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
