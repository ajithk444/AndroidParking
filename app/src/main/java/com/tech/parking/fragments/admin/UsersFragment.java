package com.tech.parking.fragments.admin;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tech.parking.R;
import com.tech.parking.adapter.MainCardAdapter;
import com.tech.parking.base.BaseFragment;
import com.tech.parking.beans.UserModel;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.listeners.RequestCallback;
import com.tech.parking.model.CardItem;
import com.tech.parking.model.MainCardItem;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends BaseFragment {
    @Override
    protected void initiateCardItem(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ArrayList<CardItem> homeItems = new ArrayList<>();
        MainCardAdapter cardAdapter = new MainCardAdapter(getActivity(), homeItems);
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.setItemClick((OnItemClick<CardItem>) getActivity());
        UserController.getInstance().allUsers(getUserId(), new RequestCallback<List<UserModel>>() {
            @Override
            public void success(List<UserModel> model) {
                cardAdapter.addAll(fetchItems(model));
            }

            @Override
            public void error(String error) {

            }
        });
    }

    private List<CardItem> fetchItems(List<UserModel> model) {
        List<CardItem> items = new ArrayList<>();
        for (UserModel user : model) {
            items.add(make(user));
        }
        return items;
    }

    private CardItem make(UserModel user) {
        return MainCardItem.Builder.makeInstance()
                .withIcon(getDrawable(R.drawable.ic_user))
                .withFragment(SingleUserFragment.newInstance(user.getUserId()))
                .withCounts(-1)
                .withTitle(user.getFullName())
                .build();
    }
}
