package com.example.sarwabbs.faceapp;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class BugReport extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);
        TextView currentDate = findViewById(R.id.timeStamp);
        currentDate.setText(Calendar.getInstance().getTime().toString());
    }

    public void cancelReport(View view)
    {
        this.finish();
    }

    public void submitReport(View view)
    {
        long runTime = getIntent().getLongExtra("runTime",99);
        long currentTime = System.currentTimeMillis();
        TextView currentDate = findViewById(R.id.timeStamp);
        TextView debugText = findViewById(R.id.debugView);
        EditText description = findViewById(R.id.description);
        Spinner location = findViewById(R.id.location);
        Spinner bugType = findViewById(R.id.bug);
        CheckBox shutdownCheck = findViewById(R.id.afterShutdownCheck);
        try {
            File bugFileDirectory = new File(getApplicationContext().getExternalFilesDir(
                    Environment.DIRECTORY_DOCUMENTS), "bugReports");
            if (!bugFileDirectory.mkdirs()) {
                //Log.e(LOG_TAG, "Directory not created");
            }
            File file = new File(bugFileDirectory, "bugReports.txt");
            PrintWriter bugFileWriter = new PrintWriter(new FileOutputStream(file, true));
            bugFileWriter.append("CurrentDate/Time: " + currentDate.getText().toString() + "\nLocation: " + location.getSelectedItem().toString() + "\nBugtype: " + bugType.getSelectedItem().toString() + "\nDescription: " + description.getText().toString() + "\nRuntime: " + Long.toString(TimeUnit.MILLISECONDS.toMinutes(currentTime - runTime) ) + " minutes\n" + "AfterShutdown: " + shutdownCheck.isChecked() + "\n\n" );
            bugFileWriter.close();
            //debugText.setText(this.getFilesDir().toString());
           // debugText.setText(Long.toString(TimeUnit.MILLISECONDS.toMinutes(currentTime - runTime)));
            this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
