package com.tech.parking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.database.Query;
import com.tech.parking.R;
import com.tech.parking.base.FirebaseRecyclerAdapter;
import com.tech.parking.beans.ParkingSpace;
import com.tech.parking.holder.SpaceHolder;
import com.tech.parking.listeners.OnItemClick;

public class ParkingSpaceAdapter extends FirebaseRecyclerAdapter<SpaceHolder, ParkingSpace> {

    private Context context;
    private OnItemClick<ParkingSpace> itemClick = item -> {

    };
    private Predicate filter = space -> true;

    public ParkingSpaceAdapter(Query query, Context context) {
        super(query);
        this.context = context;
    }

    public void setFilter(Predicate filter) {
        this.filter = filter;
    }

    @Override
    protected boolean validateItem(ParkingSpace item) {
        return filter.test(item);
    }

    public void setItemClick(OnItemClick<ParkingSpace> itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public SpaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new SpaceHolder(inflater.inflate(R.layout.item_dialog, parent, false));
    }

    @Override
    public void onBindViewHolder(SpaceHolder spaceHolder, int position) {
        spaceHolder.init(getItem(position));
        spaceHolder.itemView.setOnClickListener(v -> itemClick.onCLick(getItem(position)));
    }

    public interface Predicate {
        boolean test(ParkingSpace space);
    }
}
