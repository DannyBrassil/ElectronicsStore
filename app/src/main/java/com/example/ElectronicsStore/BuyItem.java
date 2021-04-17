package com.example.ElectronicsStore;

public class BuyItem implements OrderIF{
    private Item item;

    public BuyItem(Item item){
        this.item = item;
    }

    public void execute() {
        item.buy();
    }

}
