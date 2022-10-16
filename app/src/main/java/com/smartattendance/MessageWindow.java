package com.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class
MessageWindow extends AppCompatActivity{

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_window);
        btn = (Button)findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogin();
            }
        });
    }
    public void gotoLogin()
    {
        if(WinSelectUser.userType.equals("Teacher")) {
            Intent intent = new Intent(this, LoginTeacher.class);
            startActivity(intent);
            finish();
        }
        if(WinSelectUser.userType.equals("Student")) {
            Intent intent = new Intent(this, LoginStudent.class);
            startActivity(intent);
            finish();
        }
    }
}