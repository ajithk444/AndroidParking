package com.tech.parking.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.view.View;

import com.tech.parking.R;
import com.tech.parking.utils.ToastUtils;

public class AddModelDialog extends BottomSheetDialogFragment {

    private AddModelCallBack callBack;
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
    private int inputType = InputType.TYPE_CLASS_TEXT;

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_add_model, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        TextInputEditText editText = contentView.findViewById(R.id.inputTextName);
        editText.setInputType(inputType);
        contentView.findViewById(R.id.saveButton)
                .setOnClickListener(v -> {
                    if (editText.getText().toString().isEmpty()) {
                        ToastUtils.showToastError(getActivity(), "Name is required");
                    } else {
                        addModel(editText.getText().toString());
                        dismiss();
                    }
                });

    }

    private void addModel(String name) {
        callBack.onAdd(name);
    }

    public void setCallBack(AddModelCallBack callBack) {
        this.callBack = callBack;
    }

    public interface AddModelCallBack {
        void onAdd(String name);
    }

}

