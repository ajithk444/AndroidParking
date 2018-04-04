package com.tech.parking.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;

import com.tech.parking.R;
import com.tech.parking.adapter.ParkingAdapter;
import com.tech.parking.beans.ParkingModel;
import com.tech.parking.beans.ParkingSpace;
import com.tech.parking.controller.ParkingController;
import com.tech.parking.controller.ParkingSpaceController;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.listeners.RequestCallback;
import com.tech.parking.utils.ToastUtils;

import java.util.Date;

public class AddParkingSpaceDialog extends BottomSheetDialogFragment{

    private TextInputEditText inputSpaceName;
    private ParkingModel parkingModel;
    private Button chooseParking;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.setContentView(R.layout.fragment_add_space);
        View contentView = View.inflate(getContext(), R.layout.fragment_add_space, null);
        dialog.setContentView(contentView);
        chooseParking = contentView.findViewById(R.id.chooseParking);
        chooseParking.setOnClickListener(v -> {
            AdapterBottomSheet bottomSheet = new AdapterBottomSheet();
            ParkingAdapter parkingAdapter = new ParkingAdapter(ParkingController.getInstance().getReference(), getActivity());
            parkingAdapter.setItemClicks(item -> {
                onCLick(item);
                bottomSheet.dismiss();
            });
            bottomSheet.setAdapter(parkingAdapter);
            bottomSheet.show(getActivity().getSupportFragmentManager(), "SELECT_PARKING");
        });
        inputSpaceName = contentView.findViewById(R.id.inputSpaceName);
        contentView.findViewById(R.id.saveButton)
                .setOnClickListener(v -> {
                    if (inputSpaceName.getText().toString().isEmpty()) {
                        ToastUtils.showToastError(getActivity(), "Name is required");
                    } else if (parkingModel == null) {
                        ToastUtils.showToastError(getContext(), "Please choose park");
                    } else {
                        addSpace();
                    }
                });
    }

    private void addSpace() {
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setParkingId(parkingModel.getParkingId());
        parkingSpace.setSpaceName(inputSpaceName.getText().toString());
        parkingSpace.setSpaceState(ParkingSpace.SpaceState.Available);
        parkingSpace.setCreateDate(new Date());
        ParkingSpaceController.getInstance().add(parkingSpace, new RequestCallback<ParkingSpace>() {
            @Override
            public void success(ParkingSpace model) {
                ToastUtils.showToastSuccess(getActivity(), "Created Success");
                dismiss();
            }

            @Override
            public void error(String error) {
                ToastUtils.showToastError(getActivity(), error);
            }
        });
    }

    void onCLick(ParkingModel item) {
        chooseParking.setText(item.getParkingName());
        parkingModel = item;
    }

}
