package com.fieldforce.harmonkardonff.customer_rating_module.ui.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import app.core.base.InnosolsActivity;
import mob.field.harmonkardonff.services.WebService;

public class CustomerRatingActivity extends InnosolsActivity {

    private ImageView imageViewQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_rating);

        imageViewQR = findViewById(R.id.iv_qr_code);

        WebService webService = new WebService();
        String ratingUrl = webService.getCustomerRatingUrl();
        generateQR(ratingUrl);
    }

    private void generateQR(String content) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            imageViewQR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            ShowToast("Failed to generate QR code");
        }
    }

    @Override
    public void RegisterTableInfoForLocalDB() {
    }
}
