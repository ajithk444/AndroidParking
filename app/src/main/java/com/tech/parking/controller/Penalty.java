package com.tech.parking.controller;

public class Penalty {
    private final Long totalTime;
    private final Double totalCost;

    Penalty(Long totalTime, Double totalCost) {
        this.totalTime = totalTime;
        this.totalCost = totalCost;
    }

    Penalty() {
        this(0L, 0d);
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    @Override
    public String toString() {
        return String.format("Time : %d H, Cost : %f ₹", totalTime, totalCost);
    }
}