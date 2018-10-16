package com.barcode.vanessa.barcodereader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.MeasureFormat;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    private Button btnScanner;
    private TextView txtResult;
    private Button btnGenerate;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScanner = findViewById(R.id.scan_barcode);
        txtResult = findViewById(R.id.barcode_result);
        btnGenerate = findViewById(R.id.generate_barcode);

        context = this;

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanBarcodeActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = getLayoutInflater().inflate(R.layout.layout_barcode, null);

                ImageView imgBarcode = view.findViewById(R.id.imgBarcode);

                // Whatever you need to encode in the QR code
                String text = "https://developer.android.com/";
                //String text = "902057";
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 600,
                            600);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imgBarcode.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    Log.d("HOLA", "Error: " + e.getMessage());
                    e.printStackTrace();
                }

                builder.setView(view);
                builder.create().show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    txtResult.setText("Value: " + barcode.displayValue);
                }else{
                    txtResult.setText("Not found");
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
