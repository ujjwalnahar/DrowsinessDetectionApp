package com.example.drowsinessdetectionapp.Fragements;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.drowsinessdetectionapp.CameraActivity;
import com.example.drowsinessdetectionapp.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragement extends Fragment {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    public static final String CHANNEL_ID = "exampleServiceChannel";
    FloatingActionButton flt;
    public HomeFragement(){
        super(R.layout.main_fragement);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialCardView enableCamera = view.findViewById(R.id.enableCamera);

        enableCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCameraPermission()) {
                    enableCamera();
                } else {
                    requestPermission();
                }
            }
        });


    }
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                getActivity(),
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    private void enableCamera() {
        Intent notificationIntent = new Intent(getActivity(), CameraActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(getActivity(), CHANNEL_ID)
                        .setContentTitle("Some text")
                        .setContentText("Some Text")
                        .build();

// Notification ID cannot be 0
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivity(intent);
    }
}
