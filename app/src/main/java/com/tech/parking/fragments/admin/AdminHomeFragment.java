package com.tech.parking.fragments.admin;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tech.parking.R;
import com.tech.parking.adapter.MainCardAdapter;
import com.tech.parking.base.BaseFragment;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.model.CardItem;
import com.tech.parking.model.MainCardItem;

import java.util.ArrayList;
import java.util.List;

import static com.tech.parking.utils.Constant.ITEM_LOGOUT;
import static com.tech.parking.utils.Constant.ITEM_PARKING;
import static com.tech.parking.utils.Constant.ITEM_QR_READER;
import static com.tech.parking.utils.Constant.ITEM_SPACES;
import static com.tech.parking.utils.Constant.ITEM_USERS;

public class AdminHomeFragment extends BaseFragment {
    private OnItemClick<CardItem> itemClick;

    public void setItemClick(OnItemClick<CardItem> itemClick) {
        this.itemClick = itemClick;
    }

    private List<CardItem> getItems() {
        List<CardItem> result = new ArrayList<>();
        result.add(make(R.drawable.ic_product_hunt, ITEM_PARKING, new ParkingFragments()));
        result.add(make(R.drawable.ic_space_bar, ITEM_SPACES, new ParkingSpaceFragment()));
        result.add(make(R.drawable.ic_user, ITEM_USERS, new UsersFragment()));
        result.add(make(R.drawable.ic_qrcode, ITEM_QR_READER, new QRCodeReaderFragment()));
        CardItem logout = make(R.drawable.ic_off, ITEM_LOGOUT, null);
        result.add(logout);
        return result;
    }

    private CardItem make(int icon, String title, Fragment fragment) {
        return MainCardItem.Builder.makeInstance()
                .withIcon(getDrawable(icon))
                .withFragment(fragment)
                .withCounts(-1)
                .withTitle(title).build();
    }

    protected void initiateCardItem(RecyclerView mainRecyclerItems) {
        MainCardAdapter homeAdapter = new MainCardAdapter(getActivity(), getItems());
        homeAdapter.setItemClick(itemClick);
        mainRecyclerItems.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mainRecyclerItems.setAdapter(homeAdapter);
    }
}
