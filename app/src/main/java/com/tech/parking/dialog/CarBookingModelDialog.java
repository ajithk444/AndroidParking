package com.tech.parking.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.tech.parking.R;
import com.tech.parking.controller.UserCarBookingController;
import com.tech.parking.ocr.QRCodeGenerator;

import java.io.Serializable;

public class CarBookingModelDialog extends BottomSheetDialogFragment {
    public static final String DIALOG_MODEL = "DIALOG_MODEL";
    private DialogModel dialogModel;
    private BottomSheetBehavior.BottomSheetCallback
            mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    private Boolean stopEnabled = true;

    public static CarBookingModelDialog newInstance(DialogModel dialogModel) {

        Bundle args = new Bundle();
        args.putSerializable(DIALOG_MODEL, dialogModel);
        CarBookingModelDialog fragment = new CarBookingModelDialog();
        fragment.setArguments(args);
        fragment.setDialogModel(dialogModel);
        return fragment;
    }

    public void setDialogModel(DialogModel dialogModel) {
        this.dialogModel = dialogModel;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.added_car_dialog, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ImageView contentDialogImage = contentView.findViewById(R.id.contentDialogImage);
        TextView headerDialogText = contentView.findViewById(R.id.headerDialogText);
        TextView contentDialogText = contentView.findViewById(R.id.contentDialogText);
        View stopEnabledIcon = contentView.findViewById(R.id.stopEnabledIcon);
        if (stopEnabled) {
            stopEnabledIcon.setVisibility(View.GONE);
        } else {
            stopEnabledIcon.setVisibility(View.VISIBLE);
            stopEnabledIcon.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure to cancel?");
                builder.setNegativeButton("Cancel", (d, which) -> d.dismiss())
                        .setPositiveButton("Sure", (d, which) -> stopBooking());
                builder.create().show();
            });
        }

        headerDialogText.setText(dialogModel.headerText);
        contentDialogText.setText(dialogModel.contentText);
        try {
            contentDialogImage.setImageBitmap(QRCodeGenerator.getInstance().generate(dialogModel.contentToGenerateQRCode));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void stopBooking() {
        String generateQRCode = dialogModel.contentToGenerateQRCode;
        UserCarBookingController.getInstance().stop(generateQRCode);
    }

    public void setStopEnable(Boolean stopEnabled) {
        this.stopEnabled = stopEnabled;
    }


    public static class DialogModel implements Serializable {
        private final String headerText;
        private final String contentText;
        private final String contentToGenerateQRCode;

        public DialogModel(String headerText, String contentText, String contentToGenerateQRCode) {
            this.headerText = headerText;
            this.contentText = contentText;
            this.contentToGenerateQRCode = contentToGenerateQRCode;
        }
    }
}
