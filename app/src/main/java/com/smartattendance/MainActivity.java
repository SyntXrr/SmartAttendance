package com.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button reg,teacher_lgn, student_lgn;
    ImageView  img_reg,img_teacher_lgn, img_student_lgn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reg=(Button) findViewById(R.id.btn_reg);
        teacher_lgn=(Button) findViewById(R.id.btn_teach_lgn);
        student_lgn=(Button) findViewById(R.id.btn_std_lgn);

        img_reg = (ImageView) findViewById(R.id.img_reg);
        img_teacher_lgn = (ImageView) findViewById(R.id.img_tec);
        img_student_lgn = (ImageView) findViewById(R.id.img_std);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWinSelect();
            }
        });

        teacher_lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTeacherLogin();
            }
        });

        student_lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudentLogin();
            }
        });

        img_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWinSelect();
            }
        });

        img_teacher_lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTeacherLogin();
            }
        });

        img_student_lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudentLogin();
            }
        });
    }
    void openWinSelect(){
        Intent intent = new Intent(this, WinSelectUser.class);
        startActivity(intent);
    }
    void openTeacherLogin(){
        Intent intent = new Intent(this, LoginTeacher.class);
        startActivity(intent);
    }
    void openStudentLogin(){
        Intent intent = new Intent(this, LoginStudent.class);
        startActivity(intent);
    }
}