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
import com.tech.parking.beans.CarModel;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.model.CardItem;
import com.tech.parking.model.CardItemProvider;
import com.tech.parking.utils.ToastUtils;

public class CarAdapter extends FirebaseRecyclerAdapter<CarAdapter.Holder, CarModel> {

    private final LayoutInflater inflater;
    private CardItemProvider<CarModel> itemProvider;
    private OnItemClick<CardItem> itemClick;

    public CarAdapter(Query query, Context context, CardItemProvider<CarModel> itemProvider) {
        super(query);
        inflater = LayoutInflater.from(context);
        this.itemProvider = itemProvider;
        setItemClick(this::showToast);
    }

    private void showToast(CardItem cardItem) {
        ToastUtils.showToastSuccess(inflater.getContext(), cardItem.getTitle());
    }

    public void setItemClick(OnItemClick<CardItem> itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(itemClick, inflater.inflate(R.layout.main_user_home_items, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.init(getCardItem(getItem(position)));
    }

    private CardItem getCardItem(CarModel model) {
        return itemProvider.provide(model);
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView homeItemNumberOfNews;
        TextView homeItemName;
        ImageView homeItemIcon;
        private OnItemClick<CardItem> itemClick;

        Holder(OnItemClick<CardItem> itemClick, View itemView) {
            super(itemView);
            homeItemIcon = itemView.findViewById(R.id.homeItemIcon);
            homeItemName = itemView.findViewById(R.id.homeItemName);
            homeItemNumberOfNews = itemView.findViewById(R.id.homeItemNumberOfNews);
            this.itemClick = itemClick;
        }

        void init(CardItem item) {
            itemView.findViewById(R.id.mainLayoutCard).setOnClickListener(v -> itemClick.onCLick(item));

            homeItemIcon.setImageDrawable(item.getDrawable());
            homeItemName.setText(item.getTitle());
            if (item.getCounts() >= 0) {
                homeItemNumberOfNews.setText(String.format("%s", String.valueOf(item.getCounts())));
            } else
                itemView.findViewById(R.id.homeItemNumberLayout).setVisibility(View.GONE);
        }
    }
}
