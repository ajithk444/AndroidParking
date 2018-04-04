package com.tech.parking.controller;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.tech.parking.base.BaseController;
import com.tech.parking.beans.ChargeWallet;
import com.tech.parking.beans.ChargeWallet.ChargeType;
import com.tech.parking.beans.ChargeWallet.WalletType;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.listeners.RequestCallback;

import java.util.Date;

public class UserWalletController extends BaseController {
    private static final String TAG = UserWalletController.class.getCanonicalName();
    private static UserWalletController controller;


    private UserWalletController() {
        super("user_charge_wallets");
    }

    public static UserWalletController getInstance() {
        if (controller == null)
            controller = new UserWalletController();
        return controller;
    }

    public DatabaseReference getReference(String userId) {
        return getReference().child(userId);
    }

    public void addCashBalance(Double value, String userId, WalletType walletType, RequestCallback<ChargeWallet> callback) {
        ChargeWallet wallet = new ChargeWallet();
        wallet.setUserId(userId);
        wallet.setWalletType(walletType);
        wallet.setTotalCharge(value);
        wallet.setChargeType(ChargeType.CASH);
        wallet.setChargedDate(new Date());
        add(wallet, new RequestCallback<ChargeWallet>() {
            @Override
            public void success(ChargeWallet model) {
                callback.success(model);
                UserController.getInstance().addBalance(userId, model.getTotalCharge());
            }

            @Override
            public void error(String error) {
                Log.d(TAG, "Ouu error: " + error);
                callback.error(error);
            }
        });
    }

    public void add(ChargeWallet chargeWallet, RequestCallback<ChargeWallet> callback) {
        DatabaseReference push = getReference(chargeWallet.getUserId()).push();
        chargeWallet.setChargeId(push.getKey());
        push.setValue(chargeWallet)
                .addOnSuccessListener(aVoid -> callback.success(chargeWallet))
                .addOnFailureListener(e -> callback.error(e.getMessage()));
    }

    public void removeBalance(String userId, double balance) {
        addCashBalance(balance, userId, WalletType.BOOKING, new RequestCallback<ChargeWallet>() {
            @Override
            public void success(ChargeWallet model) {

            }

            @Override
            public void error(String error) {

            }
        });

    }
}
