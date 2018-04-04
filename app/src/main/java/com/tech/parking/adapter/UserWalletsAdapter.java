package com.tech.parking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.tech.parking.R;
import com.tech.parking.base.FirebaseRecyclerAdapter;
import com.tech.parking.beans.ChargeWallet;
import com.tech.parking.listeners.OnItemClick;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserWalletsAdapter extends FirebaseRecyclerAdapter<UserWalletsAdapter.WalletHolder, ChargeWallet> {

    private final LayoutInflater inflater;
    private OnItemClick<ChargeWallet> itemClick = item -> {

    };

    public UserWalletsAdapter(Query query, Context context) {
        super(query);
        inflater = LayoutInflater.from(context);
    }

    public void setItemClick(OnItemClick<ChargeWallet> itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public WalletHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WalletHolder(inflater.inflate(R.layout.item_wallet_charge, parent, false));
    }

    @Override
    public void onBindViewHolder(WalletHolder holder, int position) {
        holder.init(getItem(position));
        holder.itemView.setOnClickListener(v -> itemClick.onCLick(getItem(position)));
    }

    static class WalletHolder extends RecyclerView.ViewHolder {

        TextView walletChargeType;
        TextView walletChargeAmount;
        TextView walletChargeDate;

        WalletHolder(View itemView) {
            super(itemView);
            walletChargeType = itemView.findViewById(R.id.walletChargeType);
            walletChargeAmount = itemView.findViewById(R.id.walletChargeAmount);
            walletChargeDate = itemView.findViewById(R.id.walletChargeDate);
        }

        public void init(ChargeWallet item) {
            walletChargeAmount.setText(String.format("₹ %s", item.getTotalCharge()));
            Date chargedDate = item.getChargedDate();
            String format = SimpleDateFormat.getDateTimeInstance().format(chargedDate);
            walletChargeDate.setText(format);
            walletChargeType.setText(R.string.cash_type);
        }
    }

}
