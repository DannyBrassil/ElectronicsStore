package com.example.ElectronicsStore;

public interface DiscountChain {

    public void setNextChain(DiscountChain nextChain);
    public abstract boolean apply(DiscountOption option);

}
