package com.tech.parking.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tech.parking.R;
import com.tech.parking.base.BaseAdapter;
import com.tech.parking.beans.ParkingSpace;

public class SpaceHolder extends BaseAdapter.BaseViewHolder<ParkingSpace> {
    private ImageView dialogIcon;
    private TextView dialogText;
    public View mainCardDialogCard;
    public View mainCardDialogCardLayout;

    public SpaceHolder(View itemView) {
        super(itemView);
        dialogIcon = itemView.findViewById(R.id.dialogIcon);
        dialogText = itemView.findViewById(R.id.dialogText);
        mainCardDialogCard = itemView.findViewById(R.id.mainCardDialogCard);
        mainCardDialogCardLayout = itemView.findViewById(R.id.mainCardDialogCardLayout);
        dialogText.setClickable(false);
        dialogIcon.setClickable(false);
    }

    @Override
    public void init(ParkingSpace model) {
        dialogIcon.setImageResource(R.drawable.ic_space_bar);
        dialogText.setText(model.getSpaceName());
    }
}