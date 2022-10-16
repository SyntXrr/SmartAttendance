package com.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class

WinSelectUser extends AppCompatActivity {

    static String userType;
    Button cnt_as_teacher,cnt_as_std;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_select_user);

        cnt_as_teacher =(Button)findViewById(R.id.btn_cnt_as_teach);
        cnt_as_std =(Button)findViewById(R.id.btn_cnt_as_std);

        cnt_as_teacher.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                userType = "Teacher";
                openTeacherForm();
            }
        });

        cnt_as_std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType = "Student";
                openStudentForm();
            }
        });
    }


    void openTeacherForm(){
        Intent intent = new Intent(this, RegistrationFormTeacher.class);
        startActivity(intent);
        finish();
    }

    void openStudentForm(){
        Intent intent = new Intent(this, RegistrationFormStudent.class);
        startActivity(intent);
        finish();
    }
}