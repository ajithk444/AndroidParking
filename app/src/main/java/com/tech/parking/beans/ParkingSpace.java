package com.tech.parking.beans;

import java.util.Date;

public class ParkingSpace {
    private String spaceId;
    private String parkingId;
    private String spaceName;
    private SpaceState spaceState;
    private Date createDate;

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public SpaceState getSpaceState() {
        return spaceState;
    }

    public void setSpaceState(SpaceState spaceState) {
        this.spaceState = spaceState;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public enum SpaceState {
        Available, Reserved
    }
}
