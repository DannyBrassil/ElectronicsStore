package com.example.ElectronicsStore;

public class DiscountOption {

    private Order order;
    private boolean isFirstOrder;
    private boolean isStudent;

    public DiscountOption(Order order, boolean isFirstOrder, boolean isStudent) {
        this.order = order;
        this.isFirstOrder = isFirstOrder;
        this.isStudent = isStudent;
    }


    public Order getOrder() {
        return order;
    }

    public boolean isFirstOrder() {
        return isFirstOrder;
    }

    public boolean isStudent() {
        return isStudent;
    }

}
