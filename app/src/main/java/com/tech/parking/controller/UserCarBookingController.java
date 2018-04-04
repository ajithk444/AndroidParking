package com.tech.parking.controller;

import android.text.format.DateUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tech.parking.base.BaseController;
import com.tech.parking.beans.BookingState;
import com.tech.parking.beans.UserCarBooking;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.listeners.RequestCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserCarBookingController extends BaseController {
    private static final double PENALTY = 200;
    private static final String BOOKING_STATE = "bookingState";
    private static final double BER_HOUR = 100;
    private static UserCarBookingController controller;


    private UserCarBookingController() {
        super("user_car_bookings");
    }

    public static UserCarBookingController getInstance() {
        if (controller == null)
            controller = new UserCarBookingController();
        return controller;
    }

    private static double getTotalNeeds(Long totalTime, double berHour) {
        double balance = totalTime / DateUtils.HOUR_IN_MILLIS;
        return balance * berHour;
    }

    private static double getTotalPenalty(Long totalTime) {
        double balance = totalTime / DateUtils.HOUR_IN_MILLIS;
        return balance * (BER_HOUR * PENALTY);
    }

    public static Penalty findLateTimePenalty(UserCarBooking carBooking) {
        Date currentDate = new Date(System.currentTimeMillis());
        if (currentDate.after(carBooking.getEndTime()) && carBooking.getBookingState().equals(BookingState.ACTIVE)) {
            long totalTime = currentDate.getTime() - carBooking.getEndTime().getTime();
            double penalty = getTotalPenalty(totalTime);
            return new Penalty(totalTime / DateUtils.HOUR_IN_MILLIS, penalty);
        }
        return new Penalty();
    }

    private double getTotalNeeds(Date startTime, Date endTime) {
        long startTimeTime = startTime.getTime();
        long endTimeTime = endTime.getTime();
        long total = endTimeTime - startTimeTime;
        return getTotalNeeds(total, BER_HOUR);
    }

    private DatabaseReference getReference(String userId) {
        return getReference().child(userId);
    }

    public DatabaseReference getReference(String userId, String carId) {
        return getReference(userId).child(carId);
    }

    public void add(UserCarBooking carBooking, RequestCallback<UserCarBooking> callback) {
        DatabaseReference push = getReference(carBooking.getUserId(), carBooking.getCarId()).push();
        carBooking.setBookingId(push.getKey());
        carBooking.setReferenceId(generateCode());
        carBooking.setBookingState(BookingState.STOP);
        double totalNeeds = -1 * getTotalNeeds(carBooking.getStartTime(), carBooking.getEndTime());
        carBooking.setTotalSpend(totalNeeds);
        push.setValue(carBooking)
                .addOnSuccessListener(aVoid -> {
                    UserController.getInstance().addBookingRecord(carBooking.getUserId());
                    UserCarController.getInstance().addCarBooking(carBooking.getUserId(), carBooking.getCarId());
                    Log.d("Ajith", "Testing :" + carBooking.getSpaceId());
                    callback.success(carBooking);
                })
                .addOnFailureListener(e -> callback.error(e.getMessage()));
    }

    public void findBookingByCode(String referenceId, RequestCallback<UserCarBooking> callback) {
        getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        for (DataSnapshot shot : snap.getChildren()) {
                            for (DataSnapshot snapshot : shot.getChildren()) {
                                Log.d("findActiveUserParking", "onDataChange:snapshot " + snapshot.toString());
                                UserCarBooking booking = snapshot.getValue(UserCarBooking.class);
                                if (booking != null && booking.getReferenceId().equals(referenceId)) {
                                    callback.success(booking);
                                    return;
                                }
                            }
                        }
                    }
                }
                callback.error("Not Found");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.error(databaseError.getMessage());
            }
        });
    }

    public void findActiveUserParking(String userId, RequestCallback<List<UserCarBooking>> callback) {
        getReference(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserCarBooking> bookingList = new ArrayList<>();
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    for (DataSnapshot shot : dataSnapshot.getChildren()) {
                        for (DataSnapshot snapshot : shot.getChildren()) {
                            UserCarBooking booking = snapshot.getValue(UserCarBooking.class);
                            if (booking != null && (booking.getEndTime().after(new Date(System.currentTimeMillis())) || booking.getBookingState().equals(BookingState.ACTIVE))) {
                                bookingList.add(booking);
                            }
                        }
                    }
                }
                callback.success(bookingList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.error(databaseError.getMessage());
            }
        });
    }

    public void stop(String bookingCode) {
        findBookingByCode(bookingCode, new RequestCallback<UserCarBooking>() {
            @Override
            public void success(UserCarBooking booking) {
                DatabaseReference reference = getReference(booking.getUserId(), booking.getCarId()).child(booking.getBookingId());
                reference.child(BOOKING_STATE).setValue(BookingState.STOP);
            }

            @Override
            public void error(String error) {

            }
        });
    }

    public void active(String userId, String bookingCode) {
        findActiveUserParking(userId, new RequestCallback<List<UserCarBooking>>() {
            @Override
            public void success(List<UserCarBooking> model) {
                for (UserCarBooking booking : model) {
                    if (booking.getReferenceId().equals(bookingCode)) {
                        DatabaseReference reference = getReference(userId, booking.getCarId()).child(booking.getBookingId());
                        reference.child(BOOKING_STATE).setValue(BookingState.ACTIVE);
                        return;
                    }
                }
            }

            @Override
            public void error(String error) {

            }
        });
    }

    private String generateCode() {
        String timeMillis = Long.toString(System.currentTimeMillis());
        String s1 = timeMillis.substring(0, timeMillis.length() / 2);
        String s2 = timeMillis.substring(s1.length());
        Integer result = (int) (Long.valueOf(s1) + Long.valueOf(s2)
                + Math.random() * Long.valueOf(s2));
        return result + "";
    }
}
