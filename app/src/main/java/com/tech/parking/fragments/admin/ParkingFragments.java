package com.tech.parking.fragments.admin;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.tech.parking.adapter.ParkingAdapter;
import com.tech.parking.base.BaseFragment;
import com.tech.parking.beans.ParkingModel;
import com.tech.parking.controller.ParkingController;
import com.tech.parking.dialog.AddModelDialog;
import com.tech.parking.listeners.RequestCallback;

import java.util.Date;

public class ParkingFragments extends BaseFragment {
    @Override
    protected void initiateCardItem(RecyclerView recyclerView) {
        ParkingAdapter parkingAdapter = new ParkingAdapter(getReference(), getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(parkingAdapter);
    }

    private DatabaseReference getReference() {
        return ParkingController.getInstance().getReference();
    }

    @Override
    protected void initiateFab(FloatingActionButton fabCardPlus) {
        fabCardPlus.setOnClickListener(v -> {
            AddModelDialog addModelDialog = new AddModelDialog();
            addModelDialog.setCallBack(this::addParking);
            addModelDialog.show(getActivity().getSupportFragmentManager(), "AddBooking Fragment");
        });
    }

    private void addParking(String name) {
        ParkingModel model = new ParkingModel();
        model.setCreatedDate(new Date());
        model.setParkingName(name);
        ParkingController.getInstance().add(model, RequestCallback.NOP);
    }

}
