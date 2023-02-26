package com.example.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;

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

public class IncomingActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    TextView mnthViewInc, resIncAmt;
    EditText dateView, incName, incAmt;
    Button btnIncSave;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String incAmount = "incAmount";
    private String result;

    StringBuilder ss = new StringBuilder();
    String data,a,b,res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming);
        incName = (EditText) findViewById(R.id.incName);
        incAmt = (EditText) findViewById(R.id.incAmt);
        btnIncSave = (Button) findViewById(R.id.btnIncSave);
        resIncAmt = (TextView) findViewById(R.id.resIncAmt);

        String s = loadAmt();
        resIncAmt.setText(s);

        mnthViewInc = (TextView) findViewById(R.id.mnthViewInc);
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String value = sharedPreferences.getString("value", "");
        mnthViewInc.setText(value);

        dateView = (EditText) findViewById(R.id.dateView);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(IncomingActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnIncSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateView.getText().toString().equals("")) {
                    dateView.setError("Plz provide Date");
                } else if (incName.getText().toString().equals("")) {
                    incName.setError("Plz provide Name");
                } else if (incAmt.getText().toString().equals("")) {
                    incAmt.setError("Plz provide Amount");
                } else {

                    data = dateView.getText().toString();
                    ss.append("Date:");
                    ss.append(data);
                    ss.append("\n");
                    dateView.setText("");

                    data = incName.getText().toString();
                    ss.append("Name:");
                    ss.append(data);
                    ss.append("\n");
                    incName.setText("");

                    data = incAmt.getText().toString();
                    a = incAmt.getText().toString();
                    ss.append("Amount:");
                    ss.append(data);
                    ss.append("\n");
                    incAmt.setText("");

                    Toast.makeText(getApplicationContext(), ss + "Saved successfully...!", Toast.LENGTH_LONG).show();

                    b = resIncAmt.getText().toString();
                    res = addition(a,b);
                    resIncAmt.setText(res);
                    storeAmt(resIncAmt.getText().toString());

                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateView.setText(dateFormat.format(myCalendar.getTime()));
    }
    public void storeAmt(String result){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(incAmount, result);
        editor.apply();
    }
    public String loadAmt(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        result = sharedPreferences.getString(incAmount, "0");
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