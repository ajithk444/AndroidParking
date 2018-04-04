package com.tech.parking.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tech.parking.R;
import com.tech.parking.activity.LoginActivity;
import com.tech.parking.beans.UserModel;
import com.tech.parking.beans.UserType;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.model.CardItem;
import com.tech.parking.utils.FragmentController;

import static com.tech.parking.utils.Constant.ITEM_LOGOUT;

public abstract class DashboardActivity extends BaseActivity implements OnItemClick<CardItem>, ValueEventListener {
    private Fragment startupFragment;
    private FragmentController controller;

    @Override
    protected final void init(Bundle bundle) {
        TextView mainUserName = findViewById(R.id.mainUserName);
        UserModel userModel = getUserModel();
        UserController.getInstance().getReference().child(getUserId()).addValueEventListener(this);
        mainUserName.setText(userModel.getFullName());
        showUserDetails(userModel);
        controller = new FragmentController(R.id.userContainer, getSupportFragmentManager());
        startupFragment = getStartupFragment();
        if (startupFragment != null) {
            changeFragment(startupFragment);
        }
    }

    private void showUserDetails(UserModel userModel) {
        if (userModel.getUserType().equals(UserType.USER)) {
            TextView userTotalBalance = findViewById(R.id.userTotalBalance);
            userTotalBalance.setText(String.format("₹ %s", userModel.getUserBalance().toString()));
        } else {
            findViewById(R.id.balanceLayout).setVisibility(View.GONE);
        }
    }

    protected abstract Fragment getStartupFragment();

    protected final void changeFragment(Fragment fragment) {
        controller.changeFragment(fragment);
    }

    @Override
    protected final int getActivityLayout() {
        return R.layout.activity_dashboard;
    }

    @Override
    public final void onBackPressed() {
        if (!controller.getCurrent().equals(startupFragment)) {
            controller.changeFragment(startupFragment);
        } else {
            super.onBackPressed();
        }
    }


    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Do you want to logout ?")
                .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("logout", (dialog, which) -> userLogout());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void userLogout() {
        UserController.getInstance().logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onCLick(CardItem item) {
        if (item.getTitle().equals(ITEM_LOGOUT)) {
            logout();
        } else {
            Fragment fragment = item.getFragment();
            if (fragment != null)
                changeFragment(fragment);
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            UserModel userModel = dataSnapshot.getValue(UserModel.class);
            UserModel.init(userModel);
            showUserDetails(userModel);
            updateUser(userModel);
        }
    }

    protected void updateUser(UserModel userModel) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
