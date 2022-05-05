package com.example.drowsinessdetectionapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Rect;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.util.Size;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleService;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA;

public class ExampleService extends LifecycleService {
    private String CHANNEL_ID = "exampleServiceChannel";
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private TextView textView;
    private RelativeLayout container;
    private  Boolean isAnalyzing=false;
    private int i=0;
    private int sleeping=0;
    NotificationManager notificationManager;
    ServiceLifecycleOwner serviceLifecycleOwner;

    @Override
    public void onCreate() {
        super.onCreate();
        serviceLifecycleOwner=new ServiceLifecycleOwner();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent);

        startForeground(1, builder.build());
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        //container=findViewById(R.id.parentLayout);
        //textView = findViewById(R.id.orientation);
        serviceLifecycleOwner.start();
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindImageAnalysis(cameraProvider,builder);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
        //do heavy work on a background thread
        //stopSelf();
        return START_STICKY;
    }
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,"Forground Notification", NotificationManager.IMPORTANCE_DEFAULT);
             notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }


    }
    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider,NotificationCompat.Builder notification) {
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
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        //preview.setSurfaceProvider(previewView.getSurfaceProvider());
        Log.d("11111", "onSuccess: image  Example ");

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder().setTargetResolution(new Size(480, 360))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_BLOCK_PRODUCER).build();
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,
                imageAnalysis, preview);
        Log.d("11111", "onSuccess: image  Example 11 ");

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(ImageProxy imageProxy) {
                Log.d("11111", "onSuccess: image  Example 11 2");

                if (isAnalyzing) {
                    imageProxy.close();
                } else {
                    isAnalyzing=true;
                    Log.d("11111", "onSuccess: image  Example 11 2");

                    Image mediaImage = imageProxy.getImage();
                    // Toast.makeText(CameraActivity.this, "Face Detection started", Toast.LENGTH_SHORT).show();
                    if (mediaImage != null) {
                        InputImage image =
                                InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                        FaceDetector detector = FaceDetection.getClient(realTimeOpts);
                        Log.d("11111", "onSuccess: image detectedIiii example");
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
                                                            notification.setContentText("Drowsy");
                                                            notificationManager.notify(1,notification.build());
                                                            //Toast.makeText(CameraActivity.this, "Drowsy", Toast.LENGTH_SHORT).show();
                                                        } else if (sleeping > 6) {
                                                            notification.setContentText("Sleepy");
                                                            notificationManager.notify(1,notification.build());
                                                            //Toast.makeText(CameraActivity.this, "Sleeping", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    Rect bounds = face.getBoundingBox();
                                                    if(container.getChildCount()>1){
                                                        container.removeViewAt(1);
                                                    }

                                                    //CustomView customView=new CustomView(CameraActivity.this,bounds,face,previewView);
                                                    //container.addView(customView);
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
                                                //Toast.makeText(CameraActivity.this, "Face Detection failed", Toast.LENGTH_SHORT).show();
                                            }

                                        });
                    }
                }
            }
        });
    }
    @Override
    public void onDestroy() {

        stopForeground(0);
        stopSelf();
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}