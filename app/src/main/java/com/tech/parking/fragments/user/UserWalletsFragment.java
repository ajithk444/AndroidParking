package com.tech.parking.fragments.user;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tech.parking.adapter.UserWalletsAdapter;
import com.tech.parking.base.BaseFragment;
import com.tech.parking.beans.ChargeWallet;
import com.tech.parking.controller.UserWalletController;
import com.tech.parking.listeners.OnItemClick;

public class UserWalletsFragment extends BaseFragment implements OnItemClick<ChargeWallet> {
    @Override
    protected void initiateCardItem(RecyclerView recyclerView) {
        UserWalletsAdapter walletsAdapter = new UserWalletsAdapter(UserWalletController.getInstance().getReference(getUserId()), getActivity());
        walletsAdapter.setItemClick(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(walletsAdapter);
    }

    @Override
    public void onCLick(ChargeWallet item) {

    }
}
