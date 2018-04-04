package com.tech.parking.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tech.parking.R;

public class AdapterBottomSheet extends BottomSheetDialogFragment {

    private RecyclerView.Adapter adapter;

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.base_recycler, null);
        dialog.setContentView(contentView);
        RecyclerView recyclerView = contentView.findViewById(R.id.baseRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    }


}
