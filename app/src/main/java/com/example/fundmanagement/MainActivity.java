package com.example.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton mAddFab;
    TextView resAmt,mnthView;
    Button btnInc,btnOut;
    String Mnth="";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String bAmount = "bAmount";
    private String resultOut,resultIn,result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resAmt=findViewById(R.id.resAmt);
        btnInc=findViewById(R.id.btnInc);
        btnOut=findViewById(R.id.btnOut);
        mAddFab = findViewById(R.id.add_fab);

        resAmt.setText(loadAmt("bAmount"));
        resultIn= loadAmt("incAmount");
        resultOut = loadAmt("outAmount");
        storeAmt(sub(resultIn,resultOut));

        mnthView = (TextView)findViewById(R.id.mnthView);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        Mnth= sdf.format(c.getTime());
        mnthView.setText(Mnth);

        SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("value", Mnth);
        editor.apply();

        btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, IncomingActivity.class));
            }
        });
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OutgoingActivity.class));
            }
        });
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    public void storeAmt(String result){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(bAmount, result);
        editor.apply();
    }
    public String loadAmt(String pre){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        result = sharedPreferences.getString(pre, "0");
        return result;
    }

    public String sub(String in, String out){
        int a1=0,b1=0,c=0;
        a1=Integer.parseInt(in);
        b1=Integer.parseInt(out);
        c = a1-b1;
        String i = Integer.toString(c);
        return i;
    }
}