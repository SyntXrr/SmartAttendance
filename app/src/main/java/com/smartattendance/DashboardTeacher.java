package com.smartattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;

public class DashboardTeacher extends AppCompatActivity {

    DatePickerDialog picker;
    TimePickerDialog tmPicker;
    TextView txtTeacherUID,txtTeacherName;
    String teaUID, teaFName,teaLName;
    EditText ETclassname,ETclassdate, ETclasstime;
    Button btn_create;
    ImageView btn_prfl_logout;
    static String className, classDate, classTime;
    ListView classListView;

    DatabaseReference DR = FirebaseDatabase.getInstance().getReference("User").child("Teachers").child(String.valueOf(LoginTeacher.teacherUID));
    ValueEventListener valueEventListener,classListListner,newClassListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_teacher);

        ETclassname = findViewById(R.id.ETclassName);
        ETclassdate = findViewById(R.id.ETclassDate);
        ETclasstime = findViewById(R.id.ETclassTime);
        btn_create = findViewById(R.id.btn_QR);
        txtTeacherUID = findViewById(R.id.prfl_tea_prn);
        txtTeacherName = findViewById(R.id.prfl_tea_nm);
        classListView = findViewById(R.id.class_list);
        btn_prfl_logout = findViewById(R.id.btn_prfl_logout);

        //Fetching Profile Details
        valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teaUID = snapshot.child("teacherUID").getValue().toString();
                teaFName = snapshot.child("firstName").getValue().toString();
                teaLName = snapshot.child("lastName").getValue().toString();
                txtTeacherName.setText(teaFName+" "+teaLName);
                txtTeacherUID.setText(teaUID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        DR.child("Profile").addListenerForSingleValueEvent(valueEventListener);
        DR.child("Profile").removeEventListener(valueEventListener);

        //Logout
        btn_prfl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DashboardTeacher.this, MainActivity.class);
                startActivity(intent2);
               finish();
            }
        });

        //Date Picker
        ETclassdate.setInputType(InputType.TYPE_NULL);
        ETclassdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calender = Calendar.getInstance();
                int day = calender.get(Calendar.DAY_OF_MONTH);
                int month = calender.get(Calendar.MONTH);
                int year = calender.get(Calendar.YEAR);

                picker = new DatePickerDialog(DashboardTeacher.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ETclassdate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        //Time Picker
        ETclasstime.setInputType(InputType.TYPE_NULL);
        ETclasstime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calender = Calendar.getInstance();
                int hr = calender.get(Calendar.HOUR_OF_DAY);
                int minutes = calender.get(Calendar.MINUTE);

                tmPicker = new TimePickerDialog(DashboardTeacher.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ETclasstime.setText((hourOfDay-12)+":"+minute);
                    }
                },hr,minutes,false);
                tmPicker.show();
            }
        });

        //Creating New Classroom
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClass();
            }
        });

        //Created Class list
        ArrayList<String> classList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item2,classList);
        classListView.setAdapter(adapter);

        classListListner = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    classList.add(snapshot1.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DR.child("My Classes").addValueEventListener(classListListner);

        //selected class goes to attendance list
        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ClassDT= (String) classListView.getItemAtPosition(position);
                Intent intent = new Intent(DashboardTeacher.this,AttendanceDetails.class);
                intent.putExtra("ClassTeaName",teaFName+" "+teaLName);
                intent.putExtra("ClassTeaUID",teaUID);
                intent.putExtra("ClassDT",ClassDT);
                startActivity(intent);
            }
        });
    }

    //Creating New Classroom
    public void createClass(){
        className = ETclassname.getText().toString();
        classDate = ETclassdate.getText().toString();
        classTime = ETclasstime.getText().toString();
        className = className.replaceAll(" ","_");
        if(className.isEmpty() || classDate.isEmpty() ||classTime.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter Valid Class Details", Toast.LENGTH_LONG).show();
        }
        else{
            DatabaseReference DR1 = FirebaseDatabase.getInstance().getReference("User").child("Students");
            newClassListner = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String stdPRN = dataSnapshot.getKey();
                        String stdFName = dataSnapshot.child("Profile").child("firstName").getValue().toString();
                        String stdLName = dataSnapshot.child("Profile").child("lastName").getValue().toString();
                        DR.child("My Classes").child(classDate+" "+className+" "+classTime).child(stdPRN+" "+stdFName+" "+stdLName).setValue("ABSENT");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            };
            DR1.addListenerForSingleValueEvent(newClassListner);
            Toast.makeText(getApplicationContext(), "Class Created Successfully", Toast.LENGTH_LONG).show();
        }
    }
}