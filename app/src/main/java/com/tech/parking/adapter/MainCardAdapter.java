package com.tech.parking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.parking.R;
import com.tech.parking.base.BaseAdapter;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.model.CardItem;
import com.tech.parking.utils.ToastUtils;

import java.util.List;

public class MainCardAdapter extends BaseAdapter<CardItem, CardHolder> {

    private LayoutInflater inflater;
    private OnItemClick<CardItem> itemClick;

    public MainCardAdapter(Context context, List<CardItem> homeItems) {
        super(homeItems);
        this.inflater = LayoutInflater.from(context);
        setItemClick(this::showToast);
    }

    private void showToast(CardItem cardItem) {
        ToastUtils.showToastSuccess(inflater.getContext(), cardItem.getTitle());
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View result = inflater.inflate(R.layout.main_user_home_items, parent, false);
        return new CardHolder(itemClick, result);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        holder.init(getItemIn(position));
    }

    public void setItemClick(OnItemClick<CardItem> itemClick) {
        this.itemClick = itemClick;
    }

}
