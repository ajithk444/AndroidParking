package com.tech.parking.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;

import com.tech.parking.R;
import com.tech.parking.adapter.CarDialogAdapter;
import com.tech.parking.adapter.ParkingAdapter;
import com.tech.parking.adapter.ParkingSpaceAdapter;
import com.tech.parking.base.BaseActivity;
import com.tech.parking.beans.CarModel;
import com.tech.parking.beans.ParkingModel;
import com.tech.parking.beans.ParkingSpace;
import com.tech.parking.beans.UserCarBooking;
import com.tech.parking.controller.ParkingController;
import com.tech.parking.controller.ParkingSpaceController;
import com.tech.parking.controller.UserCarBookingController;
import com.tech.parking.controller.UserCarController;
import com.tech.parking.dialog.AdapterBottomSheet;
import com.tech.parking.dialog.CarBookingModelDialog;
import com.tech.parking.dialog.CarBookingModelDialog.DialogModel;
import com.tech.parking.listeners.RequestCallback;
import com.tech.parking.ui.ViewUtils;
import com.tech.parking.utils.ToastUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBookingActivity extends BaseActivity implements RequestCallback<UserCarBooking> {

    private Button choosePark;
    private Button chooseCar;
    private Button chooseSpace;
    private Button chooseDate;
    private Button chooseStartTime;
    private Button chooseEndTime;
    private CarModel selectedCarMode;
    private ParkingSpace parkingSpace;
    private Calendar startTime = Calendar.getInstance();
    private Calendar endDateTime = Calendar.getInstance();
    private Calendar dateTime = Calendar.getInstance();
    private ParkingModel parkingModel;

    @Override
    protected void init(Bundle bundle) {
        initEditors();
        initButtons();
        Date date = new Date(System.currentTimeMillis());
        endDateTime.setTime(date);
        startTime.setTime(date);
        dateTime.setTime(date);
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_add_booking;
    }

    private void initEditors() {
        chooseCar = findViewById(R.id.chooseCar);
        choosePark = findViewById(R.id.choosePark);
        chooseSpace = findViewById(R.id.chooseSpace);
        chooseDate = findViewById(R.id.chooseDate);
        chooseStartTime = findViewById(R.id.chooseStartTime);
        chooseEndTime = findViewById(R.id.chooseEndTime);

        choosePark.setOnClickListener(v -> {
            AdapterBottomSheet bottomSheet = new AdapterBottomSheet();
            ParkingAdapter parkingAdapter = new ParkingAdapter(ParkingController.getInstance().getReference(), this);
            parkingAdapter.setItemClicks(item -> {
                bottomSheet.dismiss();
                changeCurrentPark(item);
            });
            bottomSheet.setAdapter(parkingAdapter);
            bottomSheet.show(getSupportFragmentManager(), "SELECT_PARKING");
        });
        chooseDate.setOnClickListener(v -> {
            DatePickerDialog pickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                updateDate(year, month, dayOfMonth);
            }, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH));
            pickerDialog.show();
        });

        chooseStartTime.setOnClickListener(v -> {
            TimePickerDialog pickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                updateStartTime(hourOfDay, minute);
            }, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true);
            pickerDialog.show();
        });

        chooseEndTime.setOnClickListener(v -> {
            TimePickerDialog pickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                updateEndTime(hourOfDay, minute);
            }, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true);
            pickerDialog.show();
        });
        chooseCar.setOnClickListener(v -> {
            AdapterBottomSheet bottomSheet = new AdapterBottomSheet();
            CarDialogAdapter dialogAdapter = new CarDialogAdapter(UserCarController.getInstance().getReference(getUserId()), this);
            dialogAdapter.setItemClick(item -> {
                bottomSheet.dismiss();
                changeCurrentCar(item);
            });
            bottomSheet.setAdapter(dialogAdapter);
            bottomSheet.show(getSupportFragmentManager(), "SELECT_CAR");
        });
        chooseSpace.setOnClickListener(v -> {
            AdapterBottomSheet bottomSheet = new AdapterBottomSheet();
            ParkingSpaceAdapter dialogAdapter = new ParkingSpaceAdapter(ParkingSpaceController.getInstance().getReference(), this);
            dialogAdapter.setFilter(this::isAvailable);
            dialogAdapter.setItemClick(item -> {
                bottomSheet.dismiss();
                changeCurrentSpace(item);
            });
            bottomSheet.setAdapter(dialogAdapter);
            bottomSheet.show(getSupportFragmentManager(), "SELECT_SPACE");
        });
    }

    private void updateStartTime(int hourOfDay, int minute) {
        chooseStartTime.setText(formatStartTimeText(hourOfDay, minute));
        ViewUtils.show(findViewById(R.id.selectEndTimeLayout));
    }

    private void updateEndTime(int hourOfDay, int minute) {
        chooseEndTime.setText(formatEndTimeText(hourOfDay, minute));
        ViewUtils.show(findViewById(R.id.selectCarLayout));
    }

    private void updateDate(int year, int month, int dayOfMonth) {
        updateCalender(year, month, dayOfMonth, startTime);
        updateCalender(year, month, dayOfMonth, dateTime);
        updateCalender(year, month, dayOfMonth, endDateTime);
        chooseDate.setText(SimpleDateFormat.getDateInstance().format(dateTime.getTime()));
        ViewUtils.show(findViewById(R.id.selectStartTimeLayout));
    }

    private void changeCurrentPark(ParkingModel parkingModel) {
        this.parkingModel = parkingModel;
        choosePark.setText(parkingModel.getParkingName());
        ViewUtils.show(findViewById(R.id.selectDateLayout));
    }

    private boolean isAvailable(ParkingSpace space) {
        Log.d(getClass().getCanonicalName(), "isAvailable: + " + space);
        return isCurrentSelectPark(space) &&
                space.getSpaceState().equals(ParkingSpace.SpaceState.Available);
    }

    private boolean isCurrentSelectPark(ParkingSpace space) {
        return space.getParkingId().equals(parkingModel.getParkingId());
    }

    private void updateCalender(int year, int month, int dayOfMonth, Calendar calendar) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private void changeCurrentSpace(ParkingSpace item) {
        chooseSpace.setText(item.getSpaceName());
        this.parkingSpace = item;
    }

    private void changeCurrentCar(CarModel carModel) {
        chooseCar.setText(carModel.getCarName());
        selectedCarMode = carModel;
        ViewUtils.show(findViewById(R.id.selectSpaceLayout));
    }

    private String formatEndTimeText(int hourOfDay, int minute) {
        return getDateFormat(hourOfDay, minute, endDateTime);
    }

    private String formatStartTimeText(int hourOfDay, int minute) {
        return getDateFormat(hourOfDay, minute, startTime);
    }

    private String getDateFormat(int hourOfDay, int minute, Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        DateFormat dateFormat = SimpleDateFormat.getTimeInstance();
        return dateFormat.format(calendar.getTime());
    }

    private void initButtons() {
        findViewById(R.id.addBookCLose).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddBookingActivity.this);
            builder.setMessage("Are you sure to cancel?");
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Sure", (dialog, which) -> finish());
            builder.create().show();
        });
        findViewById(R.id.addBookSave).setOnClickListener(v -> {
            if (isValidContent()) {
                saveBooking();
            }
        });
    }

    private void saveBooking() {
        UserCarBooking userCarBooking = new UserCarBooking();
        userCarBooking.setCreateDate(new Date());
        userCarBooking.setUserId(selectedCarMode.getUserId());
        userCarBooking.setCarId(selectedCarMode.getCarId());
        userCarBooking.setDate(dateTime.getTime());
        userCarBooking.setStartTime(startTime.getTime());
        userCarBooking.setEndTime(endDateTime.getTime());
        userCarBooking.setSpaceId(parkingSpace.getSpaceId());
        UserCarBookingController.getInstance().add(userCarBooking, this);
    }

    private boolean isValidContent() {
        if (parkingModel != null) {
            if (selectedCarMode != null) {
                if (parkingSpace != null) {
                    if (dateTime.getTime().after(new Date(System.currentTimeMillis()))) {
                        if (startTime.getTime().before(endDateTime.getTime())) {
                            return true;
                        } else {
                            error("Start time is conflict with end time");
                        }
                    } else {
                        error(" Booking Date is invalid");
                    }
                } else {
                    error("Please select space for your car first");
                }
            } else {
                error("Please select car first");
            }
        } else {
            error("Please select parking first");
        }
        return false;
    }

    @Override
    public void success(UserCarBooking model) {
        DialogModel dialogModel = new DialogModel("Success Create Car Booking", model.getReferenceId(), model.getReferenceId());
        CarBookingModelDialog instance = CarBookingModelDialog.newInstance(dialogModel);
        instance.show(getSupportFragmentManager(), model.getBookingId());
    }

    @Override
    public void error(String error) {
        ToastUtils.showToastError(this, error);
    }
}
