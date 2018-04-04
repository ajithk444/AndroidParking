package com.tech.parking.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tech.parking.R;
import com.tech.parking.base.BaseAdapter;
import com.tech.parking.beans.CarModel;
import com.tech.parking.beans.UserCarBooking;
import com.tech.parking.controller.UserCarController;
import com.tech.parking.listeners.OnItemClick;
import com.tech.parking.listeners.RequestCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActiveBookingAdapter extends BaseAdapter<UserCarBooking, ActiveBookingAdapter.ActiveBookingHolder> {

    private final LayoutInflater inflater;
    private OnItemClick<UserCarBooking> itemClick;

    public ActiveBookingAdapter(Context context, List<UserCarBooking> itemList) {
        super(itemList);
        inflater = LayoutInflater.from(context);
    }

    public void setItemClick(OnItemClick<UserCarBooking> itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public ActiveBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActiveBookingHolder(inflater.inflate(R.layout.item_car_booking, parent, false));
    }

    class ActiveBookingHolder extends BaseAdapter.BaseViewHolder<UserCarBooking> {

        private TextView carBookingCarName;
        private TextView carBookingTime;
        private TextView carBookingTotalSpend;
        private TextView carBookingDate;

        ActiveBookingHolder(View itemView) {
            super(itemView);
            carBookingCarName = itemView.findViewById(R.id.carBookingCarName);
            carBookingTime = itemView.findViewById(R.id.carBookingTime);
            carBookingTotalSpend = itemView.findViewById(R.id.carBookingTotalSpend);
            carBookingDate = itemView.findViewById(R.id.carBookingDate);
        }

        @Override
        public void init(UserCarBooking carBooking) {
            UserCarController.getInstance().findCar(carBooking.getUserId(), carBooking.getCarId(), new RequestCallback<CarModel>() {
                @Override
                public void success(CarModel model) {
                    itemView.setOnClickListener(v -> itemClick.onCLick(carBooking));
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
