package com.example.ElectronicsStore;

import android.util.Log;

public class DiscountOver200 extends AbstractDiscountChain{
    @Override
    public boolean apply(DiscountOption option) {
        if (applyDiscountWhenOver200(option)) {
            return true;
        }
        return applyNextChain(option);

    }

    //15% off
    private boolean applyDiscountWhenOver200(DiscountOption option) {
        Order order = option.getOrder();
        if (order.getPrice() > 200) {
            Log.i("over 200", "true"+order.getPrice());
            Cart.discountApplied.setText("15% off on order over $200");

            order.applyDiscount(0.15F);
            Cart.totalPrice.setText("$"+order.getPrice());
            return true;
        }
        return false;
    }
}
