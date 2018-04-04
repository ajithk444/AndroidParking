package com.tech.parking.fragments.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tech.parking.R;
import com.tech.parking.beans.BookingState;
import com.tech.parking.beans.CarModel;
import com.tech.parking.beans.UserCarBooking;
import com.tech.parking.beans.UserModel;
import com.tech.parking.controller.ParkingSpaceController;
import com.tech.parking.controller.Penalty;
import com.tech.parking.controller.UserCarBookingController;
import com.tech.parking.controller.UserCarController;
import com.tech.parking.controller.UserWalletController;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.dialog.QRCaptureDialog;
import com.tech.parking.listeners.RequestCallback;
import com.tech.parking.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tech.parking.controller.UserCarBookingController.findLateTimePenalty;

public class QRCodeReaderFragment extends Fragment {
    private TextInputEditText bookingIdCode;
    private Button bookingSolveButton;
    private LinearLayout bookingLayoutInformation;
    private TextView bookingUserEmailAddress;
    private TextView bookingUserCarName;
    private TextView bookingStartDate;
    private TextView bookingTotalFinPenalty;
    private TextView bookingTotalSpend;
    private Button bookingEndButton;
    private Button bookingStartButton;
    private Button capture;
    private LinearLayout bookingLayoutInformationLoading;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_reader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookingIdCode = view.findViewById(R.id.bookingIdCode);
        bookingSolveButton = view.findViewById(R.id.bookingSolveButton);
        bookingLayoutInformation = view.findViewById(R.id.bookingLayoutInformation);
        bookingUserEmailAddress = view.findViewById(R.id.bookingUserEmailAddress);
        bookingUserCarName = view.findViewById(R.id.bookingUserCarName);
        bookingStartDate = view.findViewById(R.id.bookingStartDate);
        bookingTotalFinPenalty = view.findViewById(R.id.bookingTotalFinPenalty);
        bookingTotalSpend = view.findViewById(R.id.bookingTotalSpend);
        bookingLayoutInformationLoading = view.findViewById(R.id.bookingLayoutInformationLoading);
        bookingEndButton = view.findViewById(R.id.bookingEndButton);
        bookingStartButton = view.findViewById(R.id.bookingStartButton);
        capture = view.findViewById(R.id.capture);
        capture.setOnClickListener(v -> {
            QRCaptureDialog captureDialog = new QRCaptureDialog();
            captureDialog.setDataListener(data -> bookingIdCode.post(() -> bookingIdCode.setText(data)));
            captureDialog.show(getActivity().getSupportFragmentManager(), "QRCaptureDialog");
        });

        bookingSolveButton.setOnClickListener(v -> {
            if (bookingIdCode.getText().toString().isEmpty()) {
                ToastUtils.showToastError(getActivity(), "Enter Valid QR Code Reference Id");
            } else {
                showDataResult(bookingIdCode.getText().toString());
            }
        });
    }

    private void showDataResult(String data) {
        bookingLayoutInformationLoading.setVisibility(View.VISIBLE);
        bookingLayoutInformation.setVisibility(View.GONE);
        showData(data);
    }

    private void showData(String data) {
        UserCarBookingController bookingController = UserCarBookingController.getInstance();
        bookingController.findBookingByCode(data, new RequestCallback<UserCarBooking>() {
            @Override
            public void success(UserCarBooking carBooking) {
                findUser(carBooking);

            }

            @Override
            public void error(String error) {
                showError("Error Loading User Booking Details");
            }
        });
    }

    private void showError(String text) {
        bookingLayoutInformationLoading.setVisibility(View.GONE);
        bookingLayoutInformation.setVisibility(View.GONE);
        ToastUtils.showToastError(getActivity(), text);
    }

    private void findUser(UserCarBooking carBooking) {
        UserController.getInstance().findUser(carBooking.getUserId(), new RequestCallback<UserModel>() {
            @Override
            public void success(UserModel userModel) {
                findCarModel(userModel, carBooking);
            }

            @Override
            public void error(String error) {
                showError("Error Loading User Details");
            }
        });
    }

    private void findCarModel(UserModel userModel, UserCarBooking carBooking) {
        UserCarController.getInstance().findCar(userModel.getUserId(), carBooking.getCarId(), new RequestCallback<CarModel>() {
            @Override
            public void success(CarModel carModel) {
                initiateData(carModel, userModel, carBooking);
            }

            @Override
            public void error(String error) {
                showError("Error Loading User Car Details");
            }
        });
    }

    private void initiateData(CarModel carModel, UserModel userModel, UserCarBooking carBooking) {
        bookingUserEmailAddress.setText(userModel.getEmailAddress());
        bookingLayoutInformation.setVisibility(View.VISIBLE);
        bookingLayoutInformationLoading.setVisibility(View.GONE);
        bookingUserCarName.setText(carModel.getCarName());
        Date startTime = carBooking.getStartTime();
        bookingStartDate.setText(SimpleDateFormat.getDateTimeInstance().format(startTime));
        bookingTotalSpend.setText(String.format("₹ %s", carBooking.getTotalSpend()));
        Penalty penalty = findLateTimePenalty(carBooking);
        bookingTotalFinPenalty.setText(penalty.toString());

        if (carBooking.getBookingState().equals(BookingState.ACTIVE)) {
            bookingStartButton.setVisibility(View.GONE);
            bookingEndButton.setVisibility(View.VISIBLE);
        } else if (carBooking.getBookingState().equals(BookingState.STOP) && carBooking.getEndTime().before(new Date())) {
            bookingStartButton.setVisibility(View.GONE);
            bookingEndButton.setVisibility(View.GONE);
        } else if (carBooking.getEndTime().before(new Date()) && carBooking.getStartTime().before(new Date())) {
            bookingStartButton.setVisibility(View.VISIBLE);
            bookingEndButton.setVisibility(View.GONE);
        } else {
            bookingStartButton.setVisibility(View.GONE);
            bookingEndButton.setVisibility(View.GONE);
        }
        bookingEndButton.setOnClickListener(v -> {
            ParkingSpaceController.getInstance().makeItAvailable(carBooking.getSpaceId());
            UserWalletController.getInstance().removeBalance(carBooking.getUserId(), penalty.getTotalCost() + carBooking.getTotalSpend());
            UserCarBookingController.getInstance().stop(carBooking.getReferenceId());
            showDataResult(carBooking.getReferenceId());
        });
        bookingStartButton.setOnClickListener(v -> {
            ParkingSpaceController.getInstance().makeItReserved(carBooking.getSpaceId());
            UserCarBookingController.getInstance().active(carBooking.getUserId(), carBooking.getReferenceId());
            showDataResult(carBooking.getReferenceId());
        });
    }

}
