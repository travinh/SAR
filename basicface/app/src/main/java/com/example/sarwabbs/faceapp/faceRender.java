package com.example.sarwabbs.faceapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class faceRender extends AppCompatActivity {
private Handler imageHandler = new Handler();
private static boolean sleepFlag = true;
private static boolean imageFlag = true;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    long time1 = System.currentTimeMillis();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_face_render);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        imageRunnable.run();
    }

    private Runnable imageRunnable = new Runnable()
    {
        @Override
        public void run() {
            ImageView image = findViewById(R.id.bunnyFace);
            if(imageFlag)
            {
                image.setImageResource(R.drawable.happyface);
                imageFlag = false;
                imageHandler.postDelayed(this,2000);
            }
            else
            {
                image.setImageResource(R.drawable.orblogictitle);
                imageFlag = true;
                imageHandler.postDelayed(this,2000);
            }
        }
    };


    public void enterSleepReset(View view)
    {
        if (sleepFlag)
        {
            imageHandler.removeCallbacksAndMessages(null);
            imageFlag = true;
            final ImageView image = findViewById(R.id.bunnyFace);
            image.setImageResource(R.drawable.add);
            WindowManager.LayoutParams layout = getWindow().getAttributes();
            layout.screenBrightness = 0.4F;
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            getWindow().setAttributes(layout);
            sleepFlag = false;
        }
        else
        {
            WindowManager.LayoutParams layout = getWindow().getAttributes();
            layout.screenBrightness = 1.0F;
            getWindow().setAttributes(layout);
            imageRunnable.run();
            sleepFlag = true;
        }

    }

    /** Called when the user taps the Send button */
    public void submitBugReport(View view)
    {
        Intent intent = new Intent(getBaseContext(), BugReport.class);
        intent.putExtra("runTime", time1);
        startActivity(intent);

    }
}
