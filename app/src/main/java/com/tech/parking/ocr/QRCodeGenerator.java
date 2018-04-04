package com.tech.parking.ocr;


import android.graphics.Bitmap;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeGenerator {
    private static QRCodeGenerator instance;

    private QRCodeGenerator() {
    }

    public static QRCodeGenerator getInstance() {
        if (instance == null)
            instance = new QRCodeGenerator();
        return instance;
    }

    public Bitmap generate(String qrCodeContent) throws WriterException {
        QRGEncoder qrgEncoder = new QRGEncoder(qrCodeContent, null, QRGContents.Type.TEXT, 300);
        return qrgEncoder.encodeAsBitmap();
    }
}
