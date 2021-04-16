package com.example.ElectronicsStore;

public class firstOrderDiscount extends AbstractDiscountChain{
    @Override
    public boolean apply(DiscountOption inputDataForDiscountRules) {
        if (applyDiscountWhenFirstOrder(inputDataForDiscountRules)) {
            return true;
        }
        return applyNextChain(inputDataForDiscountRules);

    }

    //5% off
    private boolean applyDiscountWhenFirstOrder(DiscountOption inputDataForDiscountRules) {
        if (inputDataForDiscountRules.isFirstOrder()) {
            inputDataForDiscountRules.getOrder().applyDiscount(0.05F);
            return true;
        }
        return false;
    }
}
