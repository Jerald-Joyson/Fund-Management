package com.example.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;

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

public class IncomingActivity extends AppCompatActivity {
    final Calendar myCalendar= Calendar.getInstance();
    TextView mnthViewInc;
    EditText dateView,incName,incAmt;
    Button btnIncSave;

    StringBuilder ss = new StringBuilder();
    String data,Mnth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming);
        incName=(EditText) findViewById(R.id.incName);
        incAmt=(EditText) findViewById(R.id.incAmt);
        btnIncSave=(Button) findViewById(R.id.btnIncSave);

        mnthViewInc = (TextView)findViewById(R.id.mnthViewInc);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        Mnth= sdf.format(c.getTime());
        mnthViewInc.setText(Mnth);

        dateView=(EditText) findViewById(R.id.dateView);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(IncomingActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnIncSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (incName.getText().toString().equals(""))
                {
                    incName.setError("Plz provide Name");
                }
                else if(incAmt.getText().toString().equals(""))
                {
                    incAmt.setError("Plz provide Amount");
                }
                else if(dateView.getText().toString().equals(""))
                {
                    dateView.setError("Plz provide Date");
                }
                else{

                    data=dateView.getText().toString();
                    ss.append("Date:");
                    ss.append(data);
                    ss.append("\n");

                    data=incName.getText().toString();
                    ss.append("Name:");
                    ss.append(data);
                    ss.append("\n");

                    data=incAmt.getText().toString();
                    ss.append("Amount:");
                    ss.append(data);
                    ss.append("\n");

                    Toast.makeText(getApplicationContext(),ss +"Saved successfully...!",Toast.LENGTH_LONG).show();

                }

            }
        });
    }
    private void updateLabel(){
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateView.setText(dateFormat.format(myCalendar.getTime()));
    }
}