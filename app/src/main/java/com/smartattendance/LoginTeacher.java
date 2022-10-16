package com.smartattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginTeacher extends AppCompatActivity {

    ProgressBar TeaProgessBar;
    Button login;
    TextView frgt_View;
    EditText teaUID, Password;
    static String teacherUID, teacherPassword;
    DatabaseReference DR = FirebaseDatabase.getInstance().getReference("User").child("Teachers");
    ValueEventListener valueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_teacher);
        teaUID = (EditText) findViewById(R.id.tea_UID);
        Password =(EditText) findViewById(R.id.login_tea_password);
        login =(Button) findViewById(R.id.btn_login_tea);
        frgt_View =(TextView) findViewById(R.id.frgt_pass);
        TeaProgessBar = (ProgressBar)findViewById(R.id.TeaProgessBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherUID =teaUID.getText().toString();
                teacherPassword=Password.getText().toString();
                if(teacherUID.isEmpty() || teacherPassword.isEmpty() || teacherUID.length()!=10){
                    Toast.makeText( getApplicationContext(), "Enter Valid Credentials", Toast.LENGTH_LONG).show();
                }
                else{
                    checkUserIsExist();
                }
            }
        });

    }

    void checkUserIsExist(){
        TeaProgessBar.setVisibility(View.VISIBLE);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String upassword = snapshot.child("Profile").child("pass").getValue().toString();
                if(upassword.equals(teacherPassword)){
                    TeaProgessBar.setVisibility(View.INVISIBLE);
                    openTeacherDashboard();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    TeaProgessBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        DR.child(String.valueOf(teacherUID)).addListenerForSingleValueEvent(valueEventListener);
        DR.child(String.valueOf(teacherUID)).removeEventListener(valueEventListener);
    }

    void openTeacherDashboard(){
        Intent intent4 = new Intent(this,DashboardTeacher.class);
        startActivity(intent4);
        finish();
    }
}