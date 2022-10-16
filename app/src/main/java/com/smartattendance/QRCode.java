package com.smartattendance;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.WriterException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCode extends AppCompatActivity {

    TextView cldt;
    static String classTeaUID,classDT;
    ImageView gen_qr;
    Button btn_goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        cldt = findViewById(R.id.class_dt);

        gen_qr = findViewById(R.id.gen_qr);
        btn_goBack = findViewById(R.id.btn_gotoAttDash);

        Bundle extras = getIntent().getExtras();
        classTeaUID = extras.getString("classTeaUID");
        classDT = extras.getString("classDT");
        cldt.setText(classDT);

        generateQRCode();

        btn_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    void generateQRCode() {
        QRGEncoder qrgEncoder = new QRGEncoder(classTeaUID+" "+classDT, null, QRGContents.Type.TEXT, 1000);
        try {
            Bitmap QRBitmap = qrgEncoder.encodeAsBitmap();
            gen_qr.setImageBitmap(QRBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}

