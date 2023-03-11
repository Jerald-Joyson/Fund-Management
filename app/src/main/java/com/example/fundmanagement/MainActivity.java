package com.example.fundmanagement;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private static StringBuilder strBld = new StringBuilder();
    String[] permission = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher;
    FloatingActionButton mAddFab;
    TextView resAmt, mnthView;
    EditText fileName;
    Button btnInc, btnOut, btnPrint;
    String Mnth = "";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String bAmount = "bAmount";
    private String resultOut, resultIn, result;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resAmt=findViewById(R.id.resAmt);
        btnInc=findViewById(R.id.btnInc);
        btnOut = findViewById(R.id.btnOut);
        btnPrint = findViewById(R.id.btnPrint);
        mAddFab = findViewById(R.id.add_fab);
        fileName = (EditText) findViewById(R.id.fileName);

        resAmt.setText(loadAmt("bAmount"));

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
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    //String a = checkData();
                    if (fileName.getText().toString().equals("")) {
                        fileName.setError("Plz provide File name");
                    } else {
                        main();
                        String text = strBld.toString();
                        if (text.equals("")){
                            printMessage("Sorry, There is nothing to print..!!");
                        }else{
                            //String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo.";
                            String fName = fileName.getText().toString();
                            createPdf(fName, text);
                        }
                    }
                } else {
                    requestPermission();
                }
            }
        });
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultIn = loadAmt("incAmount");
                resultOut = loadAmt("outAmount");
                storeAmt(sub(resultIn, resultOut));
                Intent intent = getIntent();
                finish();
                startActivity(intent);
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

    public String sub(String in, String out) {
        int a1 = 0, b1 = 0, c = 0;
        a1 = Integer.parseInt(in);
        b1 = Integer.parseInt(out);
        c = a1 - b1;
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
            ActivityCompat.requestPermissions(MainActivity.this, permission, 30);

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

    public void createPdf(String fileName, String longText) {
        //String fileName = "hello5";
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        int x = 10, y = 50, lineHeight = 12;
        int availableWidth = canvas.getWidth() - 2 * x;
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(lineHeight);
        StaticLayout staticLayout = new StaticLayout(longText, textPaint, availableWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        staticLayout.draw(canvas);
        pdfDocument.finishPage(page);
        File file = new File("/sdcard/MIUI/" + fileName + ".pdf");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            pdfDocument.writeTo(outputStream);
            Toast.makeText(getApplicationContext(), "PDF file created successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error creating PDF file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    public void main() {
        String directoryPath = "/sdcard/Android/.FundManagement/";
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] subFolders = directory.listFiles(File::isDirectory);
            for (File subFolder : subFolders) {
                //System.out.println(subFolder.getName());
                String a = subFolder.getName();
                folderFind(a);
            }
        } else {
            printMessage("The specified directory does not exist or is not a directory.");
        }
    }

    public void folderFind(String monthName) {
        monthName = monthName.toUpperCase();
        boolean isMonthName = false;
        try {
            Month month = Month.valueOf(monthName);
            isMonthName = true;
            String fullName = month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
            strBld.append("Month : " + fullName + "\n");
//          System.out.printf("'%s' is a valid month name. The full name is '%s'.%n", monthName, fullName);
            String s = "/sdcard/Android/.FundManagement/" + fullName;
            fileFind(s);
        } catch (IllegalArgumentException e) {
        }
        if (!isMonthName) {
            //System.out.printf("'%s' is not a valid month name.%n", monthName);
            printMessage(" " + monthName + " is not a valid month name.");
        }
    }

    public void fileFind(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    //System.out.println(file.getName());
                    strBld.append("File : " + file.getName() + "\n");
                    String str = file.getName();
                    try {
                        String filePath = directoryPath + "/" + str;
                        String fileContent = readFromFile(filePath);
                        splitString(fileContent);
                    } catch (IOException e) {
                        printMessage("Error reading file: " + e.getMessage());
                    }
                }
            }
        } else {
            printMessage("The directory does not exist or is not a directory.");
        }
    }

    public String readFromFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        br.close();
        return sb.toString();
    }

    public void splitString(String inputString) {
        StringTokenizer tokenizer = new StringTokenizer(inputString, "*");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            strBld.append(token.trim()+"\n");
        }
    }

}