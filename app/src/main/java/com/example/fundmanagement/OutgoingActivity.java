package com.example.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
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
    TextView mnthViewOut,resOutAmt;
    EditText dateView1,outName,outAmt;
    Button btnOutSave;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String outAmount = "outAmount";
    private String result;

    StringBuilder sa = new StringBuilder();
    String data,a,b,res;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing);

        outName = (EditText) findViewById(R.id.outName);
        outAmt = (EditText) findViewById(R.id.outAmt);
        btnOutSave=(Button) findViewById(R.id.btnOutSave);
        resOutAmt = (TextView)findViewById(R.id.resOutAmt);

        String s = loadAmt();
        resOutAmt.setText(s);

        mnthViewOut = (TextView)findViewById(R.id.mnthViewOut);
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String value = sharedPreferences.getString("value","");
        mnthViewOut.setText(value);

        dateView1=(EditText) findViewById(R.id.dateView1);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabels();
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
                    dateView1.setError("Plz provide Date");
                }
                else if(outAmt.getText().toString().equals(""))
                {
                    outAmt.setError("Plz provide Amount");
                }
                else if(outName.getText().toString().equals(""))
                {
                    outName.setError("Plz provide Amount for ");
                }
                else{

                    data=dateView1.getText().toString();
                    sa.append("Date:");
                    sa.append(data);
                    sa.append("\n");
                    dateView1.setText("");

                    data=outAmt.getText().toString();
                    a = outAmt.getText().toString();
                    sa.append("Amount:");
                    sa.append(data);
                    sa.append("\n");
                    outAmt.setText("");

                    data=outName.getText().toString();
                    sa.append("Amount for:");
                    sa.append(data);
                    sa.append("\n");
                    outName.setText("");

                    Toast.makeText(getApplicationContext(),sa +"Saved successfully...",Toast.LENGTH_LONG).show();

                    b = resOutAmt.getText().toString();
                    res = addition(a,b);
                    resOutAmt.setText(res);
                    storeAmt(resOutAmt.getText().toString());
                }
            }
        });
    }

    private void updateLabels(){
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateView1.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void storeAmt(String result){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(outAmount, result);
        editor.apply();
    }
    public String loadAmt(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        result = sharedPreferences.getString(outAmount, "0");
        return result;
    }

    public String addition(String a, String b){
        int a1=0,b1=0,c=0;
        a1=Integer.parseInt(a);
        b1=Integer.parseInt(b);
        c = b1+a1;
        String i = Integer.toString(c);
        return i;
    }
}