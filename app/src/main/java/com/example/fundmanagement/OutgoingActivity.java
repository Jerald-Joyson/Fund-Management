package com.example.fundmanagement;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OutgoingActivity extends AppCompatActivity {
    String[] permission = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String filename = "Outgoing.txt";
    final Calendar myCalendar= Calendar.getInstance();
    TextView mnthViewOut,resOutAmt;
    EditText dateView1,outName,outAmt;
    Button btnOutSave;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String outAmount = "outAmount";
    private String result;

    StringBuilder sa = new StringBuilder();
    String data,a,b,res,value;
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
        value = sharedPreferences.getString("value","");
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
                if (checkPermission()) {
                    String a = checkData();
                    writeData(a);
                } else {
                    requestPermission();
                }

            }
        });
    }
    public String checkData(){
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
            sa.append(", ");
            dateView1.setText("");

            data=outAmt.getText().toString();
            a = outAmt.getText().toString();
            sa.append("Amount:");
            sa.append(data);
            sa.append(", ");
            outAmt.setText("");

            data=outName.getText().toString();
            sa.append("Amount_For:");
            sa.append(data);
            sa.append(", ");
            outName.setText("");

            //Toast.makeText(getApplicationContext(),sa +"Saved successfully...",Toast.LENGTH_LONG).show();

            b = resOutAmt.getText().toString();
            res = addition(a,b);
            resOutAmt.setText(res);
            storeAmt(resOutAmt.getText().toString());
        }
        return sa+"*\n";
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
    private void writeData(String data) {
        try {
            File f1 = new File(Environment.getExternalStoragePublicDirectory("Android/"), ".FundManagement");
            f1.mkdir();
            File f2 = new File(Environment.getExternalStoragePublicDirectory("Android/.FundManagement/"), value);
            f2.mkdir();
            String z = "Android/.FundManagement/" + value;
            File f = new File(Environment.getExternalStoragePublicDirectory(z), filename);
            FileWriter fos = new FileWriter(f, true);
            fos.write(data);
            fos.close();
            //printMessage("File Saved to" + getFilesDir() + "/" + filename +" completed...");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            printMessage("File not found..!!!");
        } catch (IOException e) {
            e.printStackTrace();
            printMessage("Error in saving..!!");
        }
    }
    public String addition(String a, String b){
        int a1=0,b1=0,c=0;
        a1=Integer.parseInt(a);
        b1=Integer.parseInt(b);
        c = b1+a1;
        String i = Integer.toString(c);
        return i;
    }
    public void printMessage(String m) {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                activityResultLauncher.launch(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activityResultLauncher.launch(intent);
            }
        } else {
            ActivityCompat.requestPermissions(OutgoingActivity.this, permission, 30);

        }
    }

    boolean checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readcheck = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            int writecheck = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            return readcheck == PackageManager.PERMISSION_GRANTED && writecheck == PackageManager.PERMISSION_GRANTED;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 30:
                if (grantResults.length > 0) {
                    boolean readper = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeper = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (readper && writeper) {
                        printMessage("Permission Granted..!");
                    } else {
                        printMessage("Permission Denied");
                    }
                } else {
                    printMessage("You Denied Permision");
                }
                break;
        }

    }
}
