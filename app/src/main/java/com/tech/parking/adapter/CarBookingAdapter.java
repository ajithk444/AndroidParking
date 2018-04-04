package com.tech.parking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.tech.parking.R;
import com.tech.parking.base.FirebaseRecyclerAdapter;
import com.tech.parking.beans.CarModel;
import com.tech.parking.beans.UserCarBooking;
import com.tech.parking.controller.UserCarController;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.listeners.RequestCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarBookingAdapter extends FirebaseRecyclerAdapter<CarBookingAdapter.Holder
        , UserCarBooking> {

    private final LayoutInflater inflater;
    private OnItemClick<UserCarBooking> itemClick = item -> {

    };

    public CarBookingAdapter(Query query, Context context) {
        super(query);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_car_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.init(getItem(position));
        holder.itemView.setOnClickListener(view -> {
            itemClick.onCLick(getItem(position));
        });
    }

    public void setItemClick(OnItemClick<UserCarBooking> itemClick) {
        this.itemClick = itemClick;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView carBookingCarName;
        TextView carBookingTime;
        TextView carBookingTotalSpend;
        TextView carBookingDate;

        Holder(View itemView) {
            super(itemView);
            carBookingCarName = itemView.findViewById(R.id.carBookingCarName);
            carBookingTime = itemView.findViewById(R.id.carBookingTime);
            carBookingTotalSpend = itemView.findViewById(R.id.carBookingTotalSpend);
            carBookingDate = itemView.findViewById(R.id.carBookingDate);
        }

        void init(UserCarBooking carBooking) {
            UserCarController.getInstance().findCar(carBooking.getUserId(), carBooking.getCarId(), new RequestCallback<CarModel>() {
                @Override
                public void success(CarModel model) {
                    carBookingCarName.setText(model.getCarName());
                    DateFormat dateFormat = SimpleDateFormat.getDateInstance();
                    carBookingDate.setText(dateFormat.format(carBooking.getDate()));
                    carBookingTime.setText(getText());
                    carBookingTotalSpend.setText(String.format("₹ %s", carBooking.getTotalSpend()));
                }

                private String getText() {
                    return getFormat(carBooking.getDate()) + " TO " + getFormat(carBooking.getStartTime());
                }

                private String getFormat(Date startDate) {
                    return DateUtils.formatDateTime(inflater.getContext(),
                            startDate.getTime(),
                            DateUtils.FORMAT_SHOW_TIME);
                }

                @Override
                public void error(String error) {

                }
            });
        }
    }
}
