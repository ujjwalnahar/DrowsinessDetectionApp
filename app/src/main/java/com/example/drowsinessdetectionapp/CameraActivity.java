package com.example.drowsinessdetectionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.app.Application;
import android.app.Notification;
import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drowsinessdetectionapp.ModelClasses.QuickAccessDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA;


public class CameraActivity extends AppCompatActivity{
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private TextView textView;
    private ImageView img_sleep;
    private RelativeLayout container;
    private  Boolean isAnalyzing=false;
    private int i=0;
    ProcessCameraProvider cameraProvider;
    private ExtendedFloatingActionButton fabPip,fabQuickAccess;
    private int sleeping=0;
    private boolean inPipMode=false;
    private MyApplication mApplication;
    MediaPlayer mediaPlayer;
List<String> listAlertTunes=new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mApplication=(MyApplication) getApplication();
        /*mApplication=new MyApplication() ;
        if (mApplication.mode == MyApplication.MODE_NONE) {
            saveDpi();
        } else {
            setDpi();
        }*/
        setContentView(R.layout.activity_camera);
        previewView = findViewById(R.id.previewView);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        container=findViewById(R.id.parentLayout);
        img_sleep=findViewById(R.id.img_sleeping);
        fabPip=findViewById(R.id.fab_Pip);
        fabQuickAccess=findViewById(R.id.fab_quick_access);
        listAlertTunes.add("R.raw.alert1");
        listAlertTunes.add("R.raw.alert2");
        listAlertTunes.add("R.raw.alert3");
        listAlertTunes.add("R.raw.alert4");
        listAlertTunes.add("R.raw.alert5");
        listAlertTunes.add("R.raw.alert6");
        listAlertTunes.add("R.raw.alert7");
        mediaPlayer=MediaPlayer.create(this,R.raw.alert4);
        fabQuickAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickAccessDialog quickAccessDialog=new QuickAccessDialog(CameraActivity.this);
                quickAccessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                quickAccessDialog.show();
            }
        });
        fabPip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Display d = getWindowManager()
                        .getDefaultDisplay();
                Point p = new Point();
                d.getSize(p);
                int width = p.x;
                int height = p.y;
                inPipMode=true;

                Rational ratio
                        = new Rational(3, 4);
                PictureInPictureParams.Builder
                        pip_Builder
                        = new PictureInPictureParams
                        .Builder();
                pip_Builder.setAspectRatio(ratio).build();
                enterPictureInPictureMode(pip_Builder.build());
            }
        });
        //textView = findViewById(R.id.orientation);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                         cameraProvider= cameraProviderFuture.get();
                        bindImageAnalysis(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(CameraActivity.this));
    }
   /* private void saveDpi() {
        Configuration configuration = getResources().getConfiguration();
        mApplication.orgDensityDpi = configuration.densityDpi;
    }
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        if (isInPictureInPictureMode) {
            mApplication.mode = MyApplication.MODE_PIP;
        } else {
            mApplication.mode = MyApplication.MODE_FULL;
        }
    }
    private void setDpi() {
        Configuration configuration = getResources().getConfiguration();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (mApplication.mode == MyApplication.MODE_PIP) {
            configuration.densityDpi = mApplication.orgDensityDpi/5 ;
        } else {
            configuration.densityDpi = mApplication.orgDensityDpi;
        }
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }*/
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
super.onResume();
    }
    @Override
    public void onPictureInPictureModeChanged (boolean isInPictureInPictureMode, Configuration newConfig) {
        if (isInPictureInPictureMode) {
            inPipMode=true;

            // Hide the full-screen UI (controls, etc.) while in picture-in-picture mode.
        } else {
            inPipMode=false;
            // Restore the full-screen UI.
        }
    }
    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider) {
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .build();

// Real-time contour detection
        FaceDetectorOptions realTimeOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        Log.d("11111", "onSuccess: image detectedppp");

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder().setTargetResolution(new Size(getScreenWidth(), getScreenHeight()))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_BLOCK_PRODUCER).build();
        cameraProvider.bindToLifecycle(this, cameraSelector,
                imageAnalysis, preview);
        Log.d("11111", "onSuccess: image detectedppp");

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(CameraActivity.this), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(ImageProxy imageProxy) {
                if (isAnalyzing) {
                    img_sleep.setVisibility(View.INVISIBLE);
                    imageProxy.close();
                } else {
                    isAnalyzing=true;
                    Image mediaImage = imageProxy.getImage();
                   // Toast.makeText(CameraActivity.this, "Face Detection started", Toast.LENGTH_SHORT).show();
                    if (mediaImage != null) {
                        InputImage image =
                                InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                        FaceDetector detector = FaceDetection.getClient(realTimeOpts);
                        Log.d("11111", "onSuccess: image detectedIiii");
                        detector.process(image)
                                .addOnSuccessListener(
                                        new OnSuccessListener<List<Face>>() {
                                            @Override
                                            public void onSuccess(List<Face> faces) {
                                                // Task completed successfully
                                                // ...
                                                Log.d("11111", "onSuccess: image detected" + i);
                                                i++;
                                                for (Face face : faces) {
                                                    if(face!=null && face.getLeftEyeOpenProbability()!=null) {
                                                        float probRight = face.getRightEyeOpenProbability();
                                                        float probLeft = face.getLeftEyeOpenProbability();
                                                        Log.d("22222", "onSuccess: "+probLeft+" "+probRight);
                                                        if (probLeft < 0.3f && probRight < 0.3f) {
                                                            sleeping++;
                                                        } else {
                                                            sleeping = 0;
                                                        }
                                                        if (sleeping >= 4 && sleeping <= 6) {
                                                            mediaPlayer.start();
                                                            img_sleep.setVisibility(View.VISIBLE);
                                                            Toast.makeText(CameraActivity.this, "Drowsy", Toast.LENGTH_SHORT).show();
                                                        } else if (sleeping > 6) {
                                                            Toast.makeText(CameraActivity.this, "Sleeping", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    Rect bounds = face.getBoundingBox();
                                                    if(container.getChildCount()>1){
                                                        container.removeViewAt(1);
                                                    }

                                                    CustomView customView=new CustomView(CameraActivity.this,bounds,face,previewView,inPipMode);
                                                    container.addView(customView);
                                                    //face.getAllContours().get()
                                                }
                                                imageProxy.close(); // 2
                                                isAnalyzing=false;
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                                imageProxy.close(); // 3
                                                isAnalyzing=false;
                                                Toast.makeText(CameraActivity.this, "Face Detection failed", Toast.LENGTH_SHORT).show();
                                            }

                                        });
                    }
                }
            }
        });
    }
    public void startService() {
        //cameraProvider.unbindAll();
        Intent serviceIntent = new Intent(this, ExampleService.class);
       /* //
        // Intent notificationIntent = new Intent(this, CameraActivity.class);
        //PendingIntent pendingIntent =
         //       PendingIntent.getActivity(this, 0, notificationIntent, 0);

        //Notification notification =
                new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle("Some text")
                        .setContentText("Some Text")
                        .build();
*/
        serviceIntent.putExtra("inputExtra", "Some Text");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}