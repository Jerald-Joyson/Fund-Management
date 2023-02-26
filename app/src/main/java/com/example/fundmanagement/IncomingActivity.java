package com.example.fundmanagement;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
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

public class IncomingActivity extends AppCompatActivity {
    String[] permission = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String filename = "Income.txt";
    final Calendar myCalendar = Calendar.getInstance();
    TextView mnthViewInc, resIncAmt;
    EditText dateView, incName, incAmt;
    Button btnIncSave;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String incAmount = "incAmount";
    private String result;

    StringBuilder ss = new StringBuilder();
    String data, a, b, res, value;

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
        value = sharedPreferences.getString("value", "");
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
                    res = addition(a, b);
                    resIncAmt.setText(res);
                    storeAmt(resIncAmt.getText().toString());

                }
                if (checkPermission()) {
                    String u = "Successfull..!";
                    writeData(u);
                } else {
                    requestPermission();
                }
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            printMessage("Permission Granted");
                        } else {
                            printMessage("permission Denied");
                        }
                    }
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateView.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void storeAmt(String result) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(incAmount, result);
        editor.apply();
    }

    public String loadAmt() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        result = sharedPreferences.getString(incAmount, "0");
        return result;
    }

    public String addition(String a, String b) {
        int a1 = 0, b1 = 0, c = 0;
        a1 = Integer.parseInt(a);
        b1 = Integer.parseInt(b);
        c = b1 + a1;
        String i = Integer.toString(c);
        return i;
    }

    private void writeData(String data) {
        try {
            File f1 = new File(Environment.getExternalStoragePublicDirectory("Android"), value);
            f1.mkdir();
            String z = "Android/" + value;
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
            ActivityCompat.requestPermissions(IncomingActivity.this, permission, 30);

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