package com.example.ElectronicsStore;

import java.util.ArrayList;
import java.util.List;

public class Command {

    private List<OrderIF> orderList = new ArrayList<OrderIF>();

    public void takeOrder(OrderIF order){
        orderList.add(order);
    }

    public void placeOrders(){

        for (OrderIF order : orderList) {
            order.execute();
        }
        orderList.clear();
    }
}

