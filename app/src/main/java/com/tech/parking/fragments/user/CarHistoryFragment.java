package com.tech.parking.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.tech.parking.R;
import com.tech.parking.activity.AddBookingActivity;
import com.tech.parking.adapter.CarBookingAdapter;
import com.tech.parking.base.BaseFragment;
import com.tech.parking.beans.UserCarBooking;
import com.tech.parking.controller.UserCarBookingController;
import com.tech.parking.dialog.CarBookingModelDialog;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.model.CardItem;
import com.tech.parking.model.CardItemProvider;
import com.tech.parking.model.MainCardItem;

public class CarHistoryFragment extends BaseFragment implements CardItemProvider<UserCarBooking>, OnItemClick<UserCarBooking> {

    private static final String CAR_ID = "CAR_ID";
    private static final String USER_ID = "USER_ID";

    public static CarHistoryFragment newInstance(String userId, String carId) {
        Bundle args = new Bundle();
        CarHistoryFragment fragment = new CarHistoryFragment();
        args.putString(CAR_ID, carId);
        args.putString(USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initiateCardItem(RecyclerView recyclerView) {
        CarBookingAdapter adapter = new CarBookingAdapter(getReference(), getActivity());
        adapter.setItemClick(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
    }

    private DatabaseReference getReference() {
        String userId = getArguments().getString(USER_ID);
        String carId = getArguments().getString(CAR_ID);
        return UserCarBookingController.getInstance().getReference(userId, carId);
    }

    @Override
    protected void initiateFab(FloatingActionButton fabCardPlus) {
        fabCardPlus.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddBookingActivity.class)));
    }

    @Override
    public CardItem provide(UserCarBooking model) {
        ImageView view = new ImageView(getActivity());
        view.setImageResource(R.drawable.ic_car);
        return MainCardItem.Builder.makeInstance()
                .withFragment(null)
                .withTitle(getCardTitle(model))
                .withIcon(view.getDrawable())
                .build();
    }

    private String getCardTitle(UserCarBooking model) {
        String start = DateUtils.formatDateTime(getActivity(),
                model.getDate().getTime(),
                DateUtils.FORMAT_SHOW_TIME);
        String end = DateUtils.formatDateTime(getActivity(),
                model.getStartTime().getTime(),
                DateUtils.FORMAT_SHOW_TIME);
        return start + " TO " + end;
    }

    @Override
    public void onCLick(UserCarBooking model) {
        CarBookingModelDialog.DialogModel dialogModel = new CarBookingModelDialog.DialogModel("Show this QR Code", model.getReferenceId(), model.getReferenceId());
        CarBookingModelDialog instance = CarBookingModelDialog.newInstance(dialogModel);
        instance.setStopEnable(Boolean.TRUE);
        instance.show(getActivity().getSupportFragmentManager(), model.getBookingId());
    }
}
