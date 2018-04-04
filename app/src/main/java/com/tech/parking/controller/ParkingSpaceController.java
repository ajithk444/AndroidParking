package com.tech.parking.controller;

import com.google.firebase.database.DatabaseReference;
import com.tech.parking.base.BaseController;
import com.tech.parking.beans.ParkingSpace;
import com.tech.parking.listeners.RequestCallback;

public class ParkingSpaceController extends BaseController {
    public static final String SPACE_STATE = "spaceState";
    private static ParkingSpaceController controller;


    private ParkingSpaceController() {
        super("parking_space");
    }

    public static ParkingSpaceController getInstance() {
        if (controller == null)
            controller = new ParkingSpaceController();
        return controller;
    }

    public void add(ParkingSpace parkingSpace, RequestCallback<ParkingSpace> callback) {
        DatabaseReference push = getReference().push();
        parkingSpace.setSpaceId(push.getKey());
        push.setValue(parkingSpace)
                .addOnSuccessListener(aVoid -> callback.success(parkingSpace))
                .addOnFailureListener(e -> callback.error(e.getMessage()));
    }

    public void makeItReserved(String parkSpaceId) {
        getReference().child(parkSpaceId).child(SPACE_STATE).setValue(ParkingSpace.SpaceState.Reserved);
    }

    public void makeItAvailable(String parkSpaceId) {
        getReference().child(parkSpaceId).child(SPACE_STATE).setValue(ParkingSpace.SpaceState.Available);
    }
}
