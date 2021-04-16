package com.example.ElectronicsStore;

public class DiscountOver50 extends AbstractDiscountChain{
    @Override
    public boolean apply(DiscountOption option) {
        if (applyDiscountWhenOver50(option)) {
            return true;
        }
        return applyNextChain(option);

    }

    //5% off
    private boolean applyDiscountWhenOver50(DiscountOption option) {
        Order order = option.getOrder();
        if (order.getPrice() > 50) {
            Cart.discountApplied.setText("5% off on order over $50");
            order.applyDiscount(0.05F);
            Cart.totalPrice.setText("$"+order.getPrice());
            return true;
        }
        return false;
    }
}