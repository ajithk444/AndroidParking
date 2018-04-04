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
import com.tech.parking.beans.CarModel;
import com.tech.parking.base.FirebaseRecyclerAdapter;
import com.tech.parking.listeners.OnItemClick;

public class CarDialogAdapter extends FirebaseRecyclerAdapter<CarDialogAdapter.DialogHolder, CarModel> {

    private final Context context;
    private OnItemClick<CarModel> itemClick;

    public CarDialogAdapter(Query query, Context context) {
        super(query);
        this.context = context;
    }

    public void setItemClick(OnItemClick<CarModel> itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public DialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dialog, parent, false);
        return new DialogHolder(view);
    }

    @Override
    public void onBindViewHolder(DialogHolder holder, int position) {
        holder.init(getItem(position));
        holder.itemView.setOnClickListener(v -> itemClick.onCLick(getItem(position)));
    }

    static class DialogHolder extends RecyclerView.ViewHolder {
        ImageView dialogIcon;
        TextView dialogText;

        DialogHolder(View itemView) {
            super(itemView);
            dialogIcon = itemView.findViewById(R.id.dialogIcon);
            dialogText = itemView.findViewById(R.id.dialogText);
            dialogText.setClickable(false);
            dialogIcon.setClickable(false);
        }

        private void init(CarModel carModel) {
            dialogText.setText(carModel.getCarName());
        }
    }
}
