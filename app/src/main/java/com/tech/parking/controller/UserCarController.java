package com.tech.parking.controller;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.tech.parking.base.BaseController;
import com.tech.parking.beans.CarModel;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.listeners.RequestCallback;

import java.util.ArrayList;
import java.util.List;

public class UserCarController extends BaseController {
    public static final String TOTAL_BOOKING = "totalBooking";
    public static final String TAG = "UserCarController";
    private static UserCarController controller;


    private UserCarController() {
        super("user_cars");
    }

    public static UserCarController getInstance() {
        if (controller == null)
            controller = new UserCarController();
        return controller;
    }

    public DatabaseReference getReference(String userId) {
        return getReference().child(userId);
    }

    public DatabaseReference getReference(String userId, String carId) {
        return getReference(userId).child(carId);
    }

    public void addCar(CarModel carModel, RequestCallback<Void> callback) {
        DatabaseReference reference = getReference(carModel.getUserId()).push();
        carModel.setCarId(reference.getKey());
        runTransaction(reference.setValue(carModel), callback);
        UserController.getInstance().addCarRecord(carModel.getUserId());
    }

    void addCarBooking(String userId, String carId) {
        getReference(userId, carId).child(TOTAL_BOOKING).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Log.d(TAG, "doTransaction: ==> " + mutableData.getValue());
                Long value = mutableData.getValue() != null ? (Long) mutableData.getValue() : 0L;
                value += 1;
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "onComplete: " + databaseError);
            }
        });
    }

    public void list(String userId, RequestCallback<List<CarModel>> callback) {
        getReference(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    List<CarModel> cars = new ArrayList<>();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    if (children != null && dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot child : children) {
                            CarModel model = child.getValue(CarModel.class);
                            cars.add(model);
                        }
                    }
                    callback.success(cars);
                } else {
                    callback.error("No Cars");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.error(databaseError.getMessage());
            }
        });
    }

    private <T> void runTransaction(Task<T> task, RequestCallback<T> callback) {
        task.addOnSuccessListener(callback::success)
                .addOnFailureListener((e) -> callback.error(e.getMessage()));
    }

    public void findCar(String userId, String carId, RequestCallback<CarModel> requestCallback) {
        getReference(userId, carId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    CarModel value = dataSnapshot.getValue(CarModel.class);
                    requestCallback.success(value);
                } else {
                    requestCallback.error("Not Found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                requestCallback.error(databaseError.getMessage());
            }
        });
    }
}
