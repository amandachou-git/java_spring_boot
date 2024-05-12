package com.example.java_spring_boot.service.Impl;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FixedPriceDiscountStrategy {

    private int fixedPrice;

    public FixedPriceDiscountStrategy(int fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public int calcDiscount(int priceA, int priceB) {
        return Math.min(priceA, priceB) - fixedPrice;
    }

    public int calcDiscount(int...prices) {
        return Arrays.stream(prices)
                .sorted()
                .limit(prices.length / 2)
                .map(price -> price - fixedPrice)
                .sum();
    }
}
