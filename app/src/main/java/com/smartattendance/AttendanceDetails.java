package com.smartattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;
import java.util.ArrayList;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class AttendanceDetails extends AppCompatActivity {

    ListView listView;
    String classTeaName,classTeaUID,classDT,className,classDate,classTime;
    TextView txt_cl_name, txt_cl_date,txt_cl_time,txt_clPR_cnt,txt_clAB_cnt;
    Button btn_down_sheet;
    ImageView gen_qr;
    ArrayList<String> list = new ArrayList<>();
    DatabaseReference DR1 = FirebaseDatabase.getInstance().getReference("User").child("Teachers");
    ValueEventListener valueEventListener;
    int ABCount=0,PRCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_details);
        gen_qr = findViewById(R.id.gen_qr);
        listView = findViewById(R.id.listView);
        txt_cl_name = findViewById(R.id.cls_nm_val);
        txt_cl_date = findViewById(R.id.cls_dt_val);
        txt_cl_time = findViewById(R.id.cls_tm_val);
        txt_clPR_cnt = findViewById(R.id.txt_clPR_cnt);
        txt_clAB_cnt = findViewById(R.id.txt_clAB_cnt);
        btn_down_sheet = findViewById(R.id.btn_down_sheet);
        btn_down_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceDetails.this,PDFGenerator.class);
                intent.putExtra("classTeacherName",classTeaName);
                intent.putExtra("classTeacherUID",classTeaUID);
                intent.putExtra("className",className);
                intent.putExtra("classDate",classDate);
                intent.putExtra("classTime",classTime);
                intent.putExtra("classPresentCount",PRCount);
                intent.putExtra("classAbsentCount",ABCount);
                intent.putExtra("AttendanceList",list);
                startActivity(intent);
            }
        });

        Bundle extras = getIntent().getExtras();
        classTeaName =extras.getString("ClassTeaName");
        classTeaUID = extras.getString("ClassTeaUID");
        classDT = extras.getString("ClassDT");

        generateQRCode();

        gen_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoBigQR();
            }
        });

        String arr[] = classDT.split(" ");
        classDate = arr[0];
        className = arr[1];
        classTime = arr[2];
        txt_cl_date.setText(classDate);
        txt_cl_name.setText(className);
        txt_cl_time.setText(classTime);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item,list);
        listView.setAdapter(adapter);

        valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int AbsentCount=0,PresentCount=0;
                list.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    list.add(snapshot1.getKey()+" "+snapshot1.getValue());
                    if(snapshot1.getValue().equals("ABSENT")){AbsentCount++;}
                    if(snapshot1.getValue().equals("PRESENT")){PresentCount++;}
                    PRCount = PresentCount;
                    ABCount = AbsentCount;
                    txt_clAB_cnt.setText(String.valueOf(AbsentCount));
                    txt_clPR_cnt.setText(String.valueOf(PresentCount));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        DR1.child(classTeaUID).child("My Classes").child(classDate+" "+className+" "+classTime).addValueEventListener(valueEventListener);
    }

    //QR code Generation
    void generateQRCode () {
        QRGEncoder qrgEncoder = new QRGEncoder(classTeaUID+" "+classDT, null, QRGContents.Type.TEXT, 1000);
        try {
            Bitmap QRBitmap = qrgEncoder.encodeAsBitmap();
            gen_qr.setImageBitmap(QRBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    void GotoBigQR(){
        Intent intent = new Intent(AttendanceDetails.this, QRCode.class);
        intent.putExtra("classDT",classDT);
        intent.putExtra("classTeaUID",classTeaUID);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DR1.child(classTeaUID).child("My Classes").child(classDate+" "+className+" "+classTime).removeEventListener(valueEventListener);
    }
}