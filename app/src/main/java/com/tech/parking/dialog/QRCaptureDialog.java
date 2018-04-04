package com.tech.parking.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.SurfaceView;
import android.view.View;

import com.tech.parking.R;
import com.tech.parking.ocr.QRDataListener;
import com.tech.parking.ocr.QReader;

import static com.tech.parking.ocr.QReader.BACK_CAM;

public class QRCaptureDialog extends BottomSheetDialogFragment {
    private SurfaceView bookingScannerSurface;
    private QReader qReader;
    private QRDataListener dataListener = data -> {

    };

    public void setDataListener(QRDataListener dataListener) {
        this.dataListener = dataListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        qReader.initAndStart(bookingScannerSurface);
    }

    @Override
    public void onPause() {
        super.onPause();
        qReader.releaseAndCleanup();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.qr_capture_dialog, null);
        dialog.setContentView(contentView);
        bookingScannerSurface = contentView.findViewById(R.id.bookingScannerSurface);

        qReader = new QReader.Builder(getActivity(), bookingScannerSurface, this::showData)
                .facing(BACK_CAM)
                .enableAutofocus(true)
                .height(bookingScannerSurface.getHeight())
                .width(bookingScannerSurface.getWidth())
                .build();
    }

    private void showData(String data) {
        dismiss();
        dataListener.onDetected(data);
    }
}
