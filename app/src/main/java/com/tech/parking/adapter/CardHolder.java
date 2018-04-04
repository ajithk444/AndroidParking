package com.tech.parking.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tech.parking.R;
import com.tech.parking.base.BaseAdapter;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.model.CardItem;

class CardHolder extends BaseAdapter.BaseViewHolder<CardItem> {
    private TextView homeItemNumberOfNews;
    private TextView homeItemName;
    private ImageView homeItemIcon;
    private OnItemClick<CardItem> itemClick;

    CardHolder(OnItemClick<CardItem> itemClick, View itemView) {
        super(itemView);
        homeItemIcon = itemView.findViewById(R.id.homeItemIcon);
        homeItemName = itemView.findViewById(R.id.homeItemName);
        homeItemNumberOfNews = itemView.findViewById(R.id.homeItemNumberOfNews);
        this.itemClick = itemClick;
    }

    public void init(CardItem item) {
        itemView.findViewById(R.id.mainLayoutCard).setOnClickListener(v -> itemClick.onCLick(item));
        homeItemIcon.setImageDrawable(item.getDrawable());
        homeItemName.setText(item.getTitle());
        if (item.getCounts() >= 0) {
            homeItemNumberOfNews.setText(String.format("%s", String.valueOf(item.getCounts())));
        } else {
            itemView.findViewById(R.id.homeItemNumberLayout).setVisibility(View.GONE);
        }
    }
}