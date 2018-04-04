package com.tech.parking.fragments.admin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.EditorInfo;

import com.google.firebase.database.Query;
import com.tech.parking.adapter.UserWalletsAdapter;
import com.tech.parking.base.BaseFragment;
import com.tech.parking.beans.ChargeWallet;
import com.tech.parking.controller.UserWalletController;
import com.tech.parking.dialog.AddModelDialog;
import com.tech.parking.listeners.RequestCallback;
import com.tech.parking.utils.ToastUtils;

public class SingleUserFragment extends BaseFragment {

    public static final String USER_ID = "USER_ID";

    public static SingleUserFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(USER_ID, userId);
        SingleUserFragment fragment = new SingleUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initiateCardItem(RecyclerView recyclerView) {
        Query query = UserWalletController.getInstance().getReference(getArguments().getString(USER_ID));
        UserWalletsAdapter walletsAdapter = new UserWalletsAdapter(query, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(walletsAdapter);
    }

    @Override
    protected void initiateFab(FloatingActionButton fabCardPlus) {
        fabCardPlus.setOnClickListener(v -> {
            AddModelDialog dialog = new AddModelDialog();
            dialog.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
            dialog.setCallBack(this::addWallet);
            dialog.show(getActivity().getSupportFragmentManager(), "AddModelDialog AddUserWallet");
        });
    }

    private void addWallet(String amount) {
        double totalCharge = Double.parseDouble(amount);
        String userId = getArguments().getString(USER_ID);
        UserWalletController.getInstance().addCashBalance(totalCharge, userId, ChargeWallet.WalletType.RECHARGE, new RequestCallback<ChargeWallet>() {
            @Override
            public void success(ChargeWallet model) {
                ToastUtils.showToastSuccess(getActivity(), "Wallet Added Succeeds");
            }

            @Override
            public void error(String error) {
                ToastUtils.showToastError(getActivity(), "Error While Adding Wallet");
            }
        });
    }
}
