package com.tech.parking.fragments.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tech.parking.R;
import com.tech.parking.adapter.MainCardAdapter;
import com.tech.parking.base.BaseFragment;
import com.tech.parking.beans.UserModel;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.model.CardItem;
import com.tech.parking.model.MainCardItem;
import com.tech.parking.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.tech.parking.utils.Constant.ITEM_BOOKING;
import static com.tech.parking.utils.Constant.ITEM_CARS;
import static com.tech.parking.utils.Constant.ITEM_LOGOUT;
import static com.tech.parking.utils.Constant.ITEM_WALLET;

public class UserHomeFragment extends BaseFragment {

    private OnItemClick<CardItem> itemClick;
    private MainCardAdapter adapter;

    public void setItemClick(OnItemClick<CardItem> itemClick) {
        this.itemClick = itemClick;
    }

    private List<CardItem> getItems(UserModel userModel) {
        List<CardItem> result = new ArrayList<>();
        CarFragment fragment = new CarFragment();
        fragment.setItemClick(itemClick);
        result.add(make(R.drawable.ic_car, ITEM_CARS, userModel.getTotalCar(), fragment));
        result.add(make(R.drawable.ic_memo, ITEM_BOOKING, userModel.getTotalBooking(), new UserActiveBookingFragment()));
        result.add(make(R.drawable.ic_credit_card, ITEM_WALLET, userModel.getTotalUserCharge(), new UserWalletsFragment()));
        CardItem logout = make(R.drawable.ic_off, ITEM_LOGOUT, -1, null);
        result.add(logout);
        return result;
    }

    public void updateUserState(UserModel userModel) {
        if (adapter != null) {
            adapter.changeAll(getItems(userModel));
        }
    }

    private CardItem make(int icon, String title, Integer counts, Fragment fragment) {
        return MainCardItem.Builder.makeInstance()
                .withIcon(getDrawable(icon))
                .withFragment(fragment)
                .withCounts(counts).withTitle(title).build();
    }

    protected void initiateCardItem(RecyclerView mainRecyclerItems) {
        UserModel userModel = getUserModel();
        adapter = new MainCardAdapter(getActivity(), getItems(userModel));
        adapter.setItemClick(getCurrentActivity());
        mainRecyclerItems.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mainRecyclerItems.setAdapter(adapter);
    }

    private OnItemClick<CardItem> getCurrentActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof OnItemClick)
            return (OnItemClick<CardItem>) activity;
        return item -> ToastUtils.showToastInfo(getActivity(), item.getTitle());
    }

}
