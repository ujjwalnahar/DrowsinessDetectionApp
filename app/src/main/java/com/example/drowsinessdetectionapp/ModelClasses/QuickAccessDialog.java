package com.example.drowsinessdetectionapp.ModelClasses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.drowsinessdetectionapp.R;

public class QuickAccessDialog extends Dialog implements DialogInterface.OnClickListener {
public Activity activity;
public Dialog dialog;
LinearLayout quickAccessGasStation,layout_restaurants,layout_hospitals,layout_parking;
    public QuickAccessDialog(@NonNull Activity context) {
        super(context);
        this.activity=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_access_dailog);
        quickAccessGasStation=findViewById(R.id.layout_gas_station);
        layout_restaurants=findViewById(R.id.layout_restaurants);
        layout_hospitals=findViewById(R.id.layout_hospitals);
        layout_parking=findViewById(R.id.layout_parking);
        layout_parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=Parking");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);
            }
        });
        layout_hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=Hospitals & clinics");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);
            }
        });
        layout_restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=restaurant");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);
            }
        });
        quickAccessGasStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=Petrol");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);

            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
