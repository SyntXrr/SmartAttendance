package com.smartattendance;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PDFGenerator extends AppCompatActivity {

    int pageHeight = 1420;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;
    private static final int PERMISSION_REQUEST_CODE = 200;
    Button btn_bck;
    String teacherName, teacherUID, className, classDate, classTime;
    int classPRCount,classABCount;
    ArrayList<String>AttendanceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfgenerator);
        btn_bck = findViewById(R.id.btn_bck);
        btn_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle= getIntent().getExtras();
        teacherName = bundle.getString("classTeacherName");
        teacherUID = bundle.getString("classTeacherUID");
        className = bundle.getString("className");
        classDate = bundle.getString("classDate");
        classTime = bundle.getString("classTime");
        classPRCount = bundle.getInt("classPresentCount");
        classABCount = bundle.getInt("classAbsentCount");
        AttendanceList = bundle.getStringArrayList("AttendanceList");

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.reporthead);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 800, 150, false);
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
        generatePDF();
    }

    private void generatePDF() {
        DateFormat reportDateTime = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date date = new Date();
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();
        Paint title1 = new Paint();
        Paint title2 = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(scaledbmp, 10, 20, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(25);
        title.setColor(ContextCompat.getColor(this, R.color.app_color));

        title1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title1.setTextSize(25);
        title1.setColor(ContextCompat.getColor(this, R.color.app_color));

        title2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title2.setTextSize(25);
        title2.setColor(ContextCompat.getColor(this, R.color.RED));

        canvas.drawText("REPORT GENERATED ON: "+reportDateTime.format(date), 120, 210, title1);

        canvas.drawLine(60,220,700,220,title1);
        canvas.drawText("Teacher Details:", 80, 260, title);
        canvas.drawText("Name: "+teacherName, 80, 290, title1);
        canvas.drawText("UID: "+teacherUID, 80, 320, title1);
        canvas.drawText("Class Details:", 500, 260, title);
        canvas.drawText(className, 500, 290, title1);
        canvas.drawText(classDate+" "+classTime, 500, 320, title1);
        canvas.drawText("Present: "+classPRCount, 500, 350, title1);
        canvas.drawText("Absent : "+classABCount, 500, 380, title2);

        canvas.drawLine(60,400,700,400,title1);
        canvas.drawText("Attenance List", 80, 440, title);

        int y=450;
        for(String stdData:AttendanceList){
            y = y+40;
            String std[] = stdData.split(" ");
            if(std[3].equals("ABSENT")) {
                canvas.drawText(std[0]+" "+std[1]+" "+std[2], 80, y, title2);
                canvas.drawText(std[3], 600, y, title2);
            }
            else{
                canvas.drawText(std[0]+" "+std[1]+" "+std[2], 80, y, title1);
                canvas.drawText(std[3], 600, y, title1);
            }
        }
        y=y+60;
        canvas.drawText("**** END OF REPORT ****", 250, y, title1);
        pdfDocument.finishPage(myPage);

        File file = new File(Environment.getExternalStorageDirectory(), "Report.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(PDFGenerator.this, "Report.pdf saved\nPlease check your File Manager.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}