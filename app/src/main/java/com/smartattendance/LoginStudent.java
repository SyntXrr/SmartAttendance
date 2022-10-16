package com.smartattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginStudent extends AppCompatActivity {
    ProgressBar StdProgessBar;
    Button login;
    EditText stdPRN, Password;
    static String studentdPRN,studentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);
        stdPRN = findViewById(R.id.std_PRN);
        Password = findViewById(R.id.login_std_password);
        login = findViewById(R.id.btn_login_std);
        StdProgessBar =  findViewById(R.id.StdProgessBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentdPRN =stdPRN.getText().toString();
                studentPassword=Password.getText().toString();
                if(stdPRN.getText().toString().isEmpty() || Password.getText().toString().isEmpty() || stdPRN.length()!=10){
                    Toast.makeText( getApplicationContext(), "Enter Valid Credentials", Toast.LENGTH_LONG).show();
                }
                else{
                    checkUserIsExist();
                }
            }
        });
    }
    void checkUserIsExist(){
        StdProgessBar.setVisibility(View.VISIBLE);
        DatabaseReference DR = FirebaseDatabase.getInstance().getReference("User").child("Students").child(String.valueOf(studentdPRN));
        DR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String upassword = snapshot.child("Profile").child("pass").getValue().toString();
                if(upassword.equals(studentPassword)){
                    StdProgessBar.setVisibility(View.INVISIBLE);
                    openStudentDashbaord();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    StdProgessBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    void openStudentDashbaord(){
        Intent intent = new Intent(this,DashboardStudent.class);
        startActivity(intent);
        finish();
    }
}