package com.tech.parking.controller;

import com.google.firebase.database.DatabaseReference;
import com.tech.parking.base.BaseController;
import com.tech.parking.beans.ParkingModel;
import com.tech.parking.listeners.RequestCallback;

public class ParkingController extends BaseController {
    private static ParkingController controller;

    private ParkingController() {
        super("parking");
    }

    public static ParkingController getInstance() {
        if (controller == null)
            controller = new ParkingController();
        return controller;
    }

    public void add(ParkingModel model, RequestCallback<Void> callback) {
        DatabaseReference push = getReference().push();
        model.setParkingId(push.getKey());
        push.setValue(model)
                .addOnSuccessListener(callback::success)
                .addOnFailureListener(e -> callback.error(e.getMessage()));
    }

}
