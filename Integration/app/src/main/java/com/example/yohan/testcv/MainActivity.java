package com.example.yohan.testcv;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.graphics.Bitmap;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.Utils;
import org.opencv.objdetect.CascadeClassifier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import android.content.Context;


public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    public CascadeClassifier face_cascade;
    private ImageClassifier classifier;
    private Handler imageHandler = new Handler();
    private static boolean sleepFlag = true;
    private static String imageString = "none";
    private static final String TAG = "MainActivity";
    long time1 = System.currentTimeMillis();
    String path;
    public void load_cascade(){
        try {
            Log.d(TAG,"I'm in\n");
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            Log.d(TAG,"Loaded crap\n");
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {

                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            Log.d(TAG,"About to get path\n");
            String testMsg = mCascadeFile.getAbsolutePath();
            Log.d(TAG,testMsg);
            face_cascade = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            path = mCascadeFile.getAbsolutePath();
            Log.d(TAG,"I got thr path\n");
            if(face_cascade.empty())
            {
                Log.d(TAG,"--(!)Error loading A\n");
                return;
            }
            else
            {
                Log.d(TAG,
                        "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to load cascade. Exception thrown: " + e);
        }
    }
    Mat mRGBa;
    Mat face;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    JavaCameraView javaCameraView;
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status)
        {
            switch (status)
            {
                case BaseLoaderCallback.SUCCESS:
                {
                    javaCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
                    javaCameraView.enableView();
                    break;
                }
                default:
                {
                    super.onManagerConnected(status);
                    break;
                }
            }
            super.onManagerConnected(status);
        }
    };
    static
    {
        if(OpenCVLoader.initDebug())
        {
            Log.d(TAG, "Opencv Success");
        }
        else
        {
            Log.d(TAG,"Failure");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        ImageView emotionView = (ImageView) findViewById((R.id.bunnyFace));
        emotionView.setImageResource(R.drawable.replaceme);

        try {
            classifier = new ImageClassifier(this);
        }
        catch (IOException e)
        {
            Log.e("Error", "Failed to initialize an image classifier");
        }
        // Example of a call to a native method
        imageRunnable.run();
    }
    
    private Runnable imageRunnable = new Runnable()
    {
        @Override
        public void run() {
            ImageView image = findViewById(R.id.bunnyFace);
            if(imageString.equalsIgnoreCase("happy"))
            {
                image.setImageResource(R.drawable.happyface);
                imageHandler.postDelayed(this,1);
            }
            else if (imageString.equalsIgnoreCase("sad"))
            {
                image.setImageResource(R.drawable.replaceme);
                imageHandler.postDelayed(this,1);
            }
            else if (imageString.equalsIgnoreCase("none"))
            {
                image.setImageResource(R.drawable.add);
                imageHandler.postDelayed(this,1);
            }
            else
            {

            }
        }
    };

    public void enterSleepReset(View view)
    {
        if (sleepFlag)
        {
            imageHandler.removeCallbacksAndMessages(null);
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

    @Override
    protected void onPause()
    {
        super.onPause();
        if(javaCameraView!= null)
            javaCameraView.disableView();
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(javaCameraView!=null)
            javaCameraView.disableView();
        classifier.close();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if(OpenCVLoader.initDebug())
        {
            Log.d(TAG, "Opencv Success");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else
        {
            Log.d(TAG,"Failure");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0,this,mLoaderCallBack);
        }
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int ctranslate(long matAddress,String mypath,long faceAddress);

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBa = new Mat(height,width,CvType.CV_8UC4);
        face = new Mat(height,width,CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {
        mRGBa.release();

    }

    @Override
    //This function gets called every camera frame and is where we call identify
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRGBa = inputFrame.rgba();
        //get the image
        load_cascade();
        //load the openCV files
        int success = ctranslate(mRGBa.getNativeObjAddr(),path,face.getNativeObjAddr());
        //attempt to crop the image
        if (success==0)
        {
            //don't call the classifier, do what you want to say no face.
            Log.e("Error", "No face found");
            imageString="none";
        }
        else {
            //create the bitmap used to identify the face
            Bitmap facebitmap;
            facebitmap = Bitmap.createBitmap(face.width(),face.height(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(face,facebitmap);
            String results = classifier.classifyFrame(facebitmap);
            String [] strArray = results.split(":");
            //call the classfier
            changeFace(strArray[0]);
            //call the classifier giving it facebitmap
            facebitmap.recycle();
        }
        return null;
    }

    public void changeFace(String emotion)
    {
        Log.e("Face Found",emotion);
        if (emotion.equalsIgnoreCase("sad"))
        {
            Log.e("Face Found","Changing Face");
            imageString="sad";
        }
        else if (emotion.equalsIgnoreCase("happy"))
        {
            imageString="happy";
        }
        else
        {
            Log.e("Face Found", "I should not be here");
        }

        //KELLEN's face change code here
    }

}