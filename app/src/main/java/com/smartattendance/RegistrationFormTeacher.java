package com.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationFormTeacher extends AppCompatActivity {

    Button submit;
    EditText firstName, lastName,teacherUid,password,cnfPassword;
    String fName, lName, pass,cnfPass;
    static String  teacherUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form_teacher);

        firstName=(EditText) findViewById(R.id.teach_first_name);
        lastName=(EditText) findViewById(R.id.teach_last_name);
        teacherUid=(EditText) findViewById(R.id.teach_uid);
        password =(EditText) findViewById(R.id.teach_password);
        cnfPassword =(EditText) findViewById(R.id.teach_cnf_password);
        submit = (Button) findViewById(R.id.btn_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fName = firstName.getText().toString();
                lName = lastName.getText().toString();
                teacherUID = teacherUid.getText().toString();
                pass = password.getText().toString();
                cnfPass = cnfPassword.getText().toString();
                if(fName.isEmpty() || lName.isEmpty() || teacherUID.isEmpty() || pass.isEmpty() || cnfPass.isEmpty() || teacherUID.length()!=10){
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Data", Toast.LENGTH_LONG).show();
                }
                else if(!pass.equals(cnfPass)){
                    Toast.makeText(getApplicationContext(), "Password Doesn't Match", Toast.LENGTH_LONG).show();}

                else{
                        NewTeacherForm NewUserDetails = new NewTeacherForm(fName, lName, teacherUID, pass);
                        FirebaseDatabase UserDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference DR = UserDatabase.getReference("User").child("Teachers").child(String.valueOf(teacherUID)).child("Profile");
                        DR.setValue(NewUserDetails);
                        openMessageWindow();
                }
            }
        });
    }

    public void openMessageWindow(){
        Intent intent = new Intent(RegistrationFormTeacher.this, MessageWindow.class);
        startActivity(intent);
        finish();
    }
}