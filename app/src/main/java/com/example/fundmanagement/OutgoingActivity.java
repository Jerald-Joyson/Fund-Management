package com.example.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OutgoingActivity extends AppCompatActivity {
    final Calendar myCalendar= Calendar.getInstance();
    TextView mnthViewOut;
    EditText dateView1,outName,outAmt;
    Button btnOutSave;

    StringBuilder ss = new StringBuilder();
    String data,Mnth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing);

        outName = (EditText) findViewById(R.id.incName);
        outAmt = (EditText) findViewById(R.id.incAmt);
        btnOutSave=(Button) findViewById(R.id.btnOutSave);

        mnthViewOut = (TextView)findViewById(R.id.mnthViewOut);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        Mnth= sdf.format(c.getTime());
        mnthViewOut.setText(Mnth);

        dateView1=(EditText) findViewById(R.id.dateView1);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        dateView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(OutgoingActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnOutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dateView1.getText().toString().equals(""))
                {
                    dateView1.setError("Plz provide Name");
                }
                else if(outAmt.getText().toString().equals(""))
                {
                    outAmt.setError("Plz provide Amount");
                }
                else if(outName.getText().toString().equals(""))
                {
                    outName.setError("Plz provide Date");
                }
                else{

                    data=dateView1.getText().toString();
                    ss.append("Date:");
                    ss.append(data);
                    ss.append("\n");

                    data=outAmt.getText().toString();
                    ss.append("Amount:");
                    ss.append(data);
                    ss.append("\n");

                    data=outName.getText().toString();
                    ss.append("Amount for:");
                    ss.append(data);
                    ss.append("\n");

                    Toast.makeText(getApplicationContext(),ss +"Registration successfully...",Toast.LENGTH_LONG).show();

                }

            }
        });

    }
    private void updateLabel(){
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateView1.setText(dateFormat.format(myCalendar.getTime()));
    }
}