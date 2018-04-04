package com.tech.parking.beans;

import java.io.Serializable;

public class UserModel implements Serializable {
    private static UserModel instance;
    private String userId;
    private String emailAddress;
    private String mobileNumber;
    private String fullName;
    private UserType userType;
    private String userImageUrl;
    private Double userBalance;
    private Integer totalCar;
    private Integer totalBooking;
    private Integer totalHistory;
    private Integer totalUserCharge;

    public static void init(UserModel model) {
        UserModel.instance = model;
    }

    public static UserModel getCurrentInstance() {
        return instance;
    }

    public Integer getTotalCar() {
        return totalCar;
    }

    public void setTotalCar(Integer totalCar) {
        this.totalCar = totalCar;
    }

    public Integer getTotalBooking() {
        return totalBooking;
    }

    public void setTotalBooking(Integer totalBooking) {
        this.totalBooking = totalBooking;
    }

    public Integer getTotalHistory() {
        return totalHistory;
    }

    public void setTotalHistory(Integer totalHistory) {
        this.totalHistory = totalHistory;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Double userBalance) {
        this.userBalance = userBalance;
    }

    public Integer getTotalUserCharge() {
        return totalUserCharge;
    }

    public void setTotalUserCharge(Integer totalUserCharge) {
        this.totalUserCharge = totalUserCharge;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }
}
