package com.tech.parking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.tech.parking.R;
import com.tech.parking.base.FirebaseRecyclerAdapter;
import com.tech.parking.beans.ParkingModel;
import com.tech.parking.listeners.OnItemClick;

public class ParkingAdapter extends FirebaseRecyclerAdapter<ParkingAdapter.ParkingHolder, ParkingModel> {

    private final LayoutInflater inflater;
    private OnItemClick<ParkingModel> itemClicks = item -> {

    };

    public ParkingAdapter(Query query, Context context) {
        super(query);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ParkingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ParkingHolder(inflater.inflate(R.layout.main_user_home_items, parent, false));
    }

    @Override
    public void onBindViewHolder(ParkingHolder holder, int position) {
        holder.init(getItem(position));
        holder.mainLayoutCard.setOnClickListener(v -> itemClicks.onCLick(getItem(position)));
    }

    public void setItemClicks(OnItemClick<ParkingModel> itemClicks) {
        this.itemClicks = itemClicks;
    }

    static class ParkingHolder extends RecyclerView.ViewHolder {
        View mainLayoutCard;
        private TextView homeItemName;
        private ImageView homeItemIcon;

        ParkingHolder(View itemView) {
            super(itemView);
            homeItemIcon = itemView.findViewById(R.id.homeItemIcon);
            homeItemName = itemView.findViewById(R.id.homeItemName);
            mainLayoutCard = itemView.findViewById(R.id.mainLayoutCard);
        }

        public void init(ParkingModel item) {
            itemView.findViewById(R.id.homeItemNumberLayout).setVisibility(View.GONE);
            homeItemIcon.setImageResource(R.drawable.ic_product_hunt);
            homeItemName.setText(item.getParkingName());
        }
    }
}
