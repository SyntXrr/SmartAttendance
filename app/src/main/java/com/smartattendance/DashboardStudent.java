package com.smartattendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class DashboardStudent extends AppCompatActivity implements View.OnClickListener{

    EditText ETclassNameDT,ETclassDateDT,ETclassTimeDT, ETstdPRN;
    String stdPRN,stdFName,stdLName,stdCName;
    TextView txtStdPRN, txtStdName;
    Button btn_scan_qr,btn_scan_ID,btn_mrk_attendance;
    String classDetails,stPRN;
    String ClTea,ClDt, ClTm, ClNm;
    String stdFullName;
    int flag =0;
    ImageView btn_prfl_lgout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_student);

        ETclassNameDT = findViewById(R.id.ETclassNameDT);
        ETclassDateDT = findViewById(R.id.ETclassDateDT);
        ETclassTimeDT = findViewById(R.id.ETclassTimeDT);
        ETstdPRN = findViewById(R.id.ETstdPRN);
        txtStdName = findViewById(R.id.prfl_std_nm);
        txtStdPRN = findViewById(R.id.prfl_std_prn);
        btn_scan_qr = findViewById(R.id.btn_scan_qr);
        btn_scan_ID = findViewById(R.id.btn_scan_ID);
        btn_mrk_attendance = findViewById(R.id.btn_mrk_attendance);
        btn_prfl_lgout = findViewById(R.id.btn_prfl_lgout);

        //Featching User Data
        DatabaseReference DR = FirebaseDatabase.getInstance().getReference("User").child("Students").child(String.valueOf(LoginStudent.studentdPRN));
        DR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stdPRN = snapshot.child("Profile").child("stdPRN").getValue().toString();
                stdFName = snapshot.child("Profile").child("firstName").getValue().toString();
                stdLName = snapshot.child("Profile").child("lastName").getValue().toString();
                stdCName = snapshot.child("Profile").child("stdCourse").getValue().toString();
                stdFullName = stdFName+" "+stdLName;
                txtStdName.setText(stdFName+" "+stdLName);
                txtStdPRN.setText(stdPRN);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Logout
        btn_prfl_lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DashboardStudent.this, MainActivity.class);
                startActivity(intent2);
                finish();
            }
        });

        //QR Scanning
        btn_scan_qr.setOnClickListener(this);
        btn_scan_ID.setOnClickListener(this);

        //Mark Attendance
        btn_mrk_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAttendance();
            }
        });
    }

    //Initiating Scanner
    @Override
    public void onClick(View v) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    //Result of Scanning
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        flag++;
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Please Scan again", Toast.LENGTH_LONG).show();
            }
            else if(flag==1){
                ETclassNameDT.setText(intentResult.getContents());
                setClassDt();
            }
            else if(flag==2){
                ETstdPRN.setText(intentResult.getContents());
                stPRN=ETstdPRN.getText().toString();
            }
            else if(flag>2){
                Toast.makeText(getBaseContext(), "Please Logout and try again", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getBaseContext
                        (), "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Fetch the class details
    public void  setClassDt(){
        classDetails = ETclassNameDT.getText().toString();
        String arr[] = classDetails.split(" ");
        ClTea = arr[0];
        ClDt = arr[1];
        ClNm = arr[2];
        ClTm = arr[3];
        ETclassNameDT.setText(ClNm);
        ETclassDateDT.setText(ClDt);
        ETclassTimeDT.setText(ClTm);
    }

    //Mark Attendance in DB
    public void markAttendance(){
        if(ETclassNameDT.getText().toString().isEmpty() || ETclassTimeDT.getText().toString().isEmpty() || ETclassTimeDT.getText().toString().isEmpty() ||
                ETstdPRN.getText().toString().isEmpty() ){
            Toast.makeText(getApplicationContext(), "Please Scan QR and ID", Toast.LENGTH_SHORT).show();
        }
        else if(stdPRN.equals(stPRN)) {
            DatabaseReference DR12 = FirebaseDatabase.getInstance().getReference("User").child("Teachers").child(ClTea).child("My Classes").child(ClDt+" "+ClNm+" "+ClTm);
            DR12.child(stdPRN+" "+stdFName+" "+stdLName).setValue("PRESENT");
            Intent intent = new Intent(this, MessageWindow2.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Please Scan only Your ID", Toast.LENGTH_SHORT).show();
        }
    }
}