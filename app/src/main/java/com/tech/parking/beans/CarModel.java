package com.tech.parking.beans;

import java.util.Date;

public class CarModel {
    private String carId;
    private String userId;
    private String carName;
    private Date addedDate;
    private Integer totalBooking;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Integer getTotalBooking() {
        return totalBooking;
    }

    public void setTotalBooking(Integer totalBooking) {
        this.totalBooking = totalBooking;
    }
}
