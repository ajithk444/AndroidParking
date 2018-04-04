package com.tech.parking.controller.authorize;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.tech.parking.base.BaseController;
import com.tech.parking.beans.UserModel;
import com.tech.parking.beans.UserType;
import com.tech.parking.listeners.RequestCallback;

import java.util.ArrayList;
import java.util.List;

public class UserController extends BaseController {
    private static final String USER_BALANCE = "userBalance";
    private static final String TOTAL_USER_CHARGE = "totalUserCharge";
    private static final String TOTAL_BOOKING = "totalBooking";
    private static final String TOTAL_CAR = "totalCar";
    private static final String TAG = UserController.class.getName();
    private static UserController userController;
    private final FirebaseAuth firebaseAuth;

    private UserController() {
        super("users");
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public static UserController getInstance() {
        if (userController == null)
            userController = new UserController();
        return userController;
    }

    private Boolean isAuthorize() {
        return getUserId() != null && firebaseAuth.getCurrentUser() != null;
    }

    public String getUserId() {
        return firebaseAuth.getUid();
    }


    public void login(String email, String password, RequestCallback<UserModel> callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    if (result != null && result.getUser() != null) {
                        findUser(result.getUser().getUid(), callback);
                    } else {
                        callback.error("User Not Found");
                    }
                })
                .addOnFailureListener(exc -> {
                    exc.printStackTrace();
                    callback.error("Please Check email and password");
                });
    }

    public void register(final UserModel model, String pass, RequestCallback<String> callback) {
        firebaseAuth.createUserWithEmailAndPassword(model.getEmailAddress(), pass)
                .addOnSuccessListener(result -> {
                    if (result != null && result.getUser() != null) {
                        model.setUserId(result.getUser().getUid());
                        createUser(model, callback);
                    } else {
                        callback.success("Error Creating User!");
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    callback.error("Please Check Information");
                });
    }

    private void createUser(final UserModel model, RequestCallback<String> callback) {
        getReference().child(model.getUserId()).setValue(model)
                .addOnSuccessListener(aVoid -> callback.success(model.getUserId()))
                .addOnFailureListener(e -> callback.error(e.getMessage()));
    }

    public void findUser(String userId, RequestCallback<UserModel> callback) {
        getReference().child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            callback.success(userModel);
                        } else
                            callback.error("User Not Found");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.error(databaseError.getMessage());
                    }
                });
    }

    public void addBalance(String userId, Double addedValue) {
        getReference().child(userId).child(USER_BALANCE).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Object mutableDataValue = mutableData.getValue();
                Log.d(TAG, "addBalance ==> doTransaction: " + mutableDataValue);
                if (mutableDataValue == null || mutableDataValue.toString().equals("0"))
                    mutableDataValue = Double.valueOf("0");
                Double value = Double.valueOf(mutableDataValue.toString());
                value += addedValue;
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "onComplete: " + databaseError);
            }
        });
        addValue(userId, TOTAL_USER_CHARGE);
    }

    private void addValue(String userId, String child) {
        getReference().child(userId).child(child).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
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

    private void removeValue(String userId, String child) {
        getReference().child(userId).child(child).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long value = mutableData.getValue() != null ? (Long) mutableData.getValue() : 0L;
                value = value - 1;
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "onComplete: " + databaseError);
            }
        });
    }


    public void allUsers(String userId, RequestCallback<List<UserModel>> callback) {
        getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserModel> userModels = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserModel model = snapshot.getValue(UserModel.class);
                    if (model != null && !model.getUserId().equals(userId) && model.getUserType().equals(UserType.USER))
                        userModels.add(model);
                }
                callback.success(userModels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onComplete: " + databaseError);
            }
        });
    }

    public void findCurrentUser(RequestCallback<UserModel> callback) {
        if (isAuthorize()) {
            findUser(getUserId(), callback);
        } else {
            callback.error("Not Register");
        }
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public void addBookingRecord(String userId) {
        addValue(userId, TOTAL_BOOKING);
    }

    public void addCarRecord(String userId) {
        addValue(userId, TOTAL_CAR);
    }
}
