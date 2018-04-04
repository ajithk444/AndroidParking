package com.tech.parking.fragments.user;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.tech.parking.R;
import com.tech.parking.adapter.CarAdapter;
import com.tech.parking.base.BaseFragment;
import com.tech.parking.beans.CarModel;
import com.tech.parking.controller.UserCarController;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.dialog.AddModelDialog;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.listeners.RequestCallback;
import com.tech.parking.model.CardItem;
import com.tech.parking.model.CardItemProvider;
import com.tech.parking.model.MainCardItem;
import com.tech.parking.utils.ToastUtils;

import java.util.Date;

public class CarFragment extends BaseFragment implements CardItemProvider<CarModel> {

    private OnItemClick<CardItem> itemClick;

    public void setItemClick(OnItemClick<CardItem> itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    protected void initiateCardItem(RecyclerView recyclerView) {
        CarAdapter carAdapter = new CarAdapter(getReference(), getActivity(), this);
        carAdapter.setItemClick(itemClick);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(carAdapter);
    }

    @Override
    protected void initiateFab(FloatingActionButton fabCardPlus) {
        fabCardPlus.setOnClickListener(v -> {
            AddModelDialog addModelDialog = new AddModelDialog();
            addModelDialog.setCallBack(this::addCar);
            addModelDialog.show(getActivity().getSupportFragmentManager(), "AddCar Fragment");
        });
    }

    private void addCar(String carName) {
        ToastUtils.showToastInfo(getActivity(), "Adding Car");
        UserCarController controller = UserCarController.getInstance();
        CarModel carModel = new CarModel();
        carModel.setAddedDate(new Date());
        carModel.setUserId(UserController.getInstance().getUserId());
        carModel.setTotalBooking(0);
        carModel.setCarName(carName);
        controller.addCar(carModel, RequestCallback.NOP);

    }

    private DatabaseReference getReference() {
        return UserCarController.getInstance().getReference(getUserId());
    }

    @Override
    public CardItem provide(CarModel model) {
        ImageView view = new ImageView(getActivity());
        view.setImageResource(R.drawable.ic_car);
        return MainCardItem.Builder.makeInstance()
                .withFragment(CarHistoryFragment.newInstance(model.getUserId(), model.getCarId()))
                .withTitle(model.getCarName())
                .withCounts(model.getTotalBooking())
                .withIcon(view.getDrawable()).build();
    }
}
