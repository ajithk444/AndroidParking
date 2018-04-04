package com.tech.parking.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public abstract class BaseAdapter<T, VH extends BaseAdapter.BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {
    private List<T> itemList;

    protected BaseAdapter(List<T> itemList) {
        this.itemList = itemList;
    }


    public void add(T model) {
        itemList.add(model);
        notifyItemChanged(getIndexOf(model));
    }

    public final void add(int index, T model) {
        itemList.add(index, model);
        notifyItemChanged(getIndexOf(model));
    }


    public final void addAll(List<T> itemList) {
        int oldPosition = itemList.size();
        this.itemList.addAll(itemList);
        notifyItemRangeInserted(oldPosition, itemList.size());
    }

    public void changeAll(List<T> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public final void update(T model, boolean addIfNotExist) {
        int indexOf = getIndexOf(model);
        if (indexOf >= 0)
            itemList.set(indexOf, model);
        if (addIfNotExist)
            add(model);
    }

    private int getIndexOf(T model) {
        return itemList.indexOf(model);
    }

    public final void remove(T model) {
        int indexOf = getIndexOf(model);
        if (itemList.remove(model))
            notifyItemRemoved(indexOf);
    }

    public void remove(int index) {
        remove(getItemIn(index));
    }

    protected T getItemIn(int position) {
        return itemList.get(position);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.init(getItemIn(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static abstract class BaseViewHolder<M> extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void init(M model);
    }
}
