package com.smartattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class
RegistrationFormStudent extends AppCompatActivity {

    Button submit;
    EditText firstName, lastName,studentPRN,studentCourse,password,cnfPassword;
    String fname, lname, stdPRN,stdCourse,stdPass,stdCnfPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form_student);
        submit = (Button) findViewById(R.id.btn_login_std);
        firstName=(EditText) findViewById(R.id.std_first_name);
        lastName=(EditText) findViewById(R.id.std_last_name);
        studentPRN=(EditText) findViewById(R.id.std_prn);
        studentCourse =(EditText)findViewById(R.id.std_course);
        password =(EditText) findViewById(R.id.std_password);
        cnfPassword =(EditText)findViewById(R.id.std_cnf_password);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = firstName.getText().toString();
                lname = lastName.getText().toString();
                stdPRN = studentPRN.getText().toString();
                stdCourse = studentCourse.getText().toString();
                stdCourse = stdCourse.replaceAll(" ","_");
                stdPass = password.getText().toString();
                stdCnfPass = cnfPassword.getText().toString();

                if( fname.isEmpty() || lname.isEmpty() || stdPRN.isEmpty() || stdPass.isEmpty()
                        || stdCnfPass.isEmpty() || (stdPRN.length()!=10)  ){
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Data", Toast.LENGTH_LONG).show();
                }
                else if(!stdPass.equals(stdCnfPass)) {
                    Toast.makeText(getApplicationContext(), "Password Doesn't Match", Toast.LENGTH_LONG).show();}
                else{
                    NewStudentForm NewUserDetails = new NewStudentForm(fname,lname,stdPRN,stdCourse,stdPass);
                    FirebaseDatabase UserDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference DR = UserDatabase.getReference("User").child("Students").child(String.valueOf(stdPRN)).child("Profile");
                    DR.setValue(NewUserDetails);
                    openMessageWindow();
                }
            }
        });

    }
    void openMessageWindow()
    {
        Intent intent = new Intent(this, MessageWindow.class);
        startActivity(intent);
        finish();
    }
}