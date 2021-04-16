package com.example.ElectronicsStore;

public abstract class AbstractDiscountChain implements DiscountChain{

    //next element in chain
    protected DiscountChain nextChain;


    public void setNextChain(DiscountChain nextChain) {
        this.nextChain = nextChain;
    }

    public boolean applyNextChain(DiscountOption option) {
        if (this.nextChain != null) {
            return this.nextChain.apply(option);
        }
        return false;
    }

}
