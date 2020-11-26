package com.dummy.dummyStrorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String dummyFile = "dummyFile.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);

        Button method1 = new Button(this);
        method1.setText("Method1");
        Button method2 = new Button(this);
        method2.setText("method2");
        root.addView(method1);
        root.addView(method2);
        setContentView(root);

        method1.setOnClickListener(v -> writWithMethod1());

        method2.setOnClickListener(v -> {
            //check for write permission handle it in activity result
            if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                return;
            }
            writWithMethod2();
        });
    }

    private void writWithMethod1() {
        File root = getExternalFilesDir(null);
        File fileDirectory = new File(root, "DummyStorage");
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdirs()) {
                Log.e(TAG, "Error: Failed to Create Directory");
            }
        }
        try {
            File fileToWrite = new File(fileDirectory, dummyFile);
            //delete old file
            if(fileToWrite.exists()) fileToWrite.delete();

            if (fileToWrite.createNewFile()) {
                FileOutputStream outPutStream = new FileOutputStream(fileToWrite);
                OutputStreamWriter outPutStreamWriter = new OutputStreamWriter(outPutStream);
                outPutStreamWriter.append("Hello world");
                outPutStreamWriter.close();
                outPutStream.flush();
                outPutStream.close();
                showToast(fileToWrite.getAbsolutePath());
            } else {
                Log.e(TAG,"Error: Failed to Create Log File");
            }

        } catch (IOException e) {
            Log.e(TAG, "Error: File write failed: ", e);
        }
    }


    private void writWithMethod2() {
        File fileDirectory = new File(Environment.getExternalStorageDirectory(), "DummyStorage");
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdirs()) {
                Log.e(TAG, "Error: Failed to Create Directory");
            }
        }
        try {
            File fileToWrite = new File(fileDirectory, dummyFile);
            //delete old file
            if(fileToWrite.exists()) fileToWrite.delete();

            if (fileToWrite.createNewFile()) {
                FileOutputStream outPutStream = new FileOutputStream(fileToWrite);
                OutputStreamWriter outPutStreamWriter = new OutputStreamWriter(outPutStream);
                outPutStreamWriter.append("Hello world");
                outPutStreamWriter.close();
                outPutStream.flush();
                outPutStream.close();
                showToast(fileToWrite.getAbsolutePath());
            } else {
                Log.e(TAG,"Error: Failed to Create Log File");
            }

        } catch (IOException e) {
            Log.e(TAG, "Error: File write failed: ", e);
        }
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults == null) {
            grantResults = new int[0];
        }

        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (requestCode == 200) {
            if (!granted) {
                //permission not granted
                //fo your stuff to warn user
            } else {
                //permission granted
                writWithMethod2();
            }
        }
    }
}