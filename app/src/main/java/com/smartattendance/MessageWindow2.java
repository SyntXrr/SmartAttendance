package com.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MessageWindow2 extends AppCompatActivity {

    Button btn_lg_out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_window2);
        btn_lg_out =findViewById(R.id.btn_lg_out);
        btn_lg_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openLogin();
            }
        });
    }

    private void openLogin() {
        Intent intent = new Intent(this, LoginStudent.class);
        startActivity(intent);
        finish();
    }
}