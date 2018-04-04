package com.tech.parking.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tech.parking.ParkingApp;
import com.tech.parking.R;
import com.tech.parking.beans.UserModel;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.ui.EmptyRecyclerView;

public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EmptyRecyclerView mainRecyclerItems = view.findViewById(R.id.mainRecyclerItems);
        FloatingActionButton fabCardPlus = view.findViewById(R.id.fabCardPlus);
        View emptyView = view.findViewById(R.id.emptyView);
        mainRecyclerItems.setEmptyView(emptyView);
        initiateCardItem(mainRecyclerItems);
        initiateFab(fabCardPlus);
    }

    protected Drawable getDrawable(int icon) {
        ImageView view;
        if (getActivity() != null)
            view = new ImageView(getActivity());
        else if (getContext() != null)
            view = new ImageView(getContext());
        else
            view = new ImageView(ParkingApp.getInstance());
        view.setImageResource(icon);
        return view.getDrawable();
    }

    protected void initiateFab(FloatingActionButton fabCardPlus) {
        fabCardPlus.setVisibility(View.GONE);
    }


    protected String getUserId() {
        return UserController.getInstance().getUserId();
    }

    protected abstract void initiateCardItem(RecyclerView recyclerView);

    protected UserModel getUserModel() {
        UserModel userModel = ((DashboardActivity) getActivity()).getUserModel();
        if (userModel == null)
            userModel = UserModel.getCurrentInstance();
        return userModel;
    }
}
