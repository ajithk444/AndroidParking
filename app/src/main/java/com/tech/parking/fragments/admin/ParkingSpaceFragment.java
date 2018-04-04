package com.tech.parking.fragments.admin;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tech.parking.adapter.ParkingSpaceAdapter;
import com.tech.parking.base.BaseFragment;
import com.tech.parking.controller.ParkingSpaceController;
import com.tech.parking.dialog.AddParkingSpaceDialog;


public class ParkingSpaceFragment extends BaseFragment {
    @Override
    protected void initiateCardItem(RecyclerView recyclerView) {
        ParkingSpaceAdapter dialogAdapter = new ParkingSpaceAdapter(ParkingSpaceController.getInstance().getReference(), getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(dialogAdapter);
    }

    @Override
    protected void initiateFab(FloatingActionButton fabCardPlus) {
        fabCardPlus.setOnClickListener(v -> {
            AddParkingSpaceDialog spaceDialog = new AddParkingSpaceDialog();
            spaceDialog.show(getFragmentManager(), "AddSpace");
        });
    }
}
