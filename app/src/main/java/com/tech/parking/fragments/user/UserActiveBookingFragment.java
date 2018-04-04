package com.tech.parking.fragments.user;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tech.parking.adapter.ActiveBookingAdapter;
import com.tech.parking.base.BaseFragment;
import com.tech.parking.beans.BookingState;
import com.tech.parking.beans.UserCarBooking;
import com.tech.parking.controller.UserCarBookingController;
import com.tech.parking.dialog.CarBookingModelDialog;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.listeners.RequestCallback;
import com.tech.parking.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class UserActiveBookingFragment extends BaseFragment implements OnItemClick<UserCarBooking> {

    @Override
    protected void initiateCardItem(RecyclerView recyclerView) {
        UserCarBookingController.getInstance().findActiveUserParking(getUserId(), new RequestCallback<List<UserCarBooking>>() {
            @Override
            public void success(List<UserCarBooking> model) {
                new Handler().postDelayed(() -> {
                    if (getActivity() != null) {
                        List<UserCarBooking> list = filterModel(model);
                        ActiveBookingAdapter adapter = new ActiveBookingAdapter(getActivity(), list);
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        adapter.setItemClick(UserActiveBookingFragment.this);
                        recyclerView.setAdapter(adapter);
                    }
                }, 1000);
            }

            private List<UserCarBooking> filterModel(List<UserCarBooking> model) {
                List<UserCarBooking> list = new ArrayList<>();
                for (UserCarBooking booking : model) {
                    if (booking.getBookingState().equals(BookingState.STOP))
                        list.add(booking);
                }
                return list;
            }

            @Override
            public void error(String error) {
                ToastUtils.showToastError(getActivity(), error);
            }
        });
    }

    @Override
    public void onCLick(UserCarBooking model) {
        CarBookingModelDialog.DialogModel dialogModel = new CarBookingModelDialog.DialogModel("Show this QR Code", model.getReferenceId(), model.getReferenceId());
        CarBookingModelDialog instance = CarBookingModelDialog.newInstance(dialogModel);
        instance.setStopEnable(Boolean.TRUE);
        instance.show(getActivity().getSupportFragmentManager(), model.getBookingId());
    }
}
