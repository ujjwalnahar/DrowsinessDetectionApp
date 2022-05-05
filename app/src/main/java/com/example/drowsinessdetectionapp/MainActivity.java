package com.example.drowsinessdetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.ImageCapture;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.drowsinessdetectionapp.Fragements.HomeFragement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment=new HomeFragement();
    private Toolbar homeToolbar;
    NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeToolbar=findViewById(R.id.homeToolbar);
        nav_view=findViewById(R.id.nav_view);
setSupportActionBar(homeToolbar);
if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.home_fragement_containter, fragment, null)
                    .commit();
        }
nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
if(id==R.id.item1){
    Intent intent=new Intent(MainActivity.this, SettingActivity.class);
    startActivity(intent);
}
        return true;
    }
});
    }
    public void startService(View v) {
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
    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
    }
}