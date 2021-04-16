package com.example.ElectronicsStore;

public class DiscountOver100 extends AbstractDiscountChain{
    @Override
    public boolean apply(DiscountOption option) {
        if (applyDiscountWhenOver100(option)) {
            return true;
        }
        return applyNextChain(option);

    }

    //10% off
    private boolean applyDiscountWhenOver100(DiscountOption option) {
        Order order = option.getOrder();
        if (order.getPrice() > 100) {
            Cart.discountApplied.setText("10% off on order over $100");
            order.applyDiscount(0.1F);
            Cart.totalPrice.setText("$"+order.getPrice());
            return true;
        }
        return false;
    }
}