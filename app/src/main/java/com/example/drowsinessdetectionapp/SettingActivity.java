package com.example.drowsinessdetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    RadioGroup tunes_radio_group;
    SeekBar volumeSeekBar;
    int volume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        volumeSeekBar=findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setProgress(50);
        volume=volumeSeekBar.getProgress();

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume=volumeSeekBar.getProgress();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tunes_radio_group=findViewById(R.id.tunes_radio_group);
    tunes_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String s=Integer.toString(checkedId);
            Toast.makeText(SettingActivity.this, s, Toast.LENGTH_SHORT).show();
            if(checkedId==R.id.radio_1){
                MediaPlayer mediaPlayer= MediaPlayer.create(SettingActivity.this,R.raw.alert1);
                mediaPlayer.setVolume(volume,volume);
                mediaPlayer.start();
            }
            else if(checkedId==R.id.radio_2){
                MediaPlayer mediaPlayer= MediaPlayer.create(SettingActivity.this,R.raw.alert2);
                mediaPlayer.setVolume(volume,volume);
                mediaPlayer.start();
            }
            else if(checkedId==R.id.radio_3){
                MediaPlayer mediaPlayer= MediaPlayer.create(SettingActivity.this,R.raw.alert3);
                mediaPlayer.setVolume(volume,volume);
                mediaPlayer.start();
            }
            else if(checkedId==R.id.radio_4){
                MediaPlayer mediaPlayer= MediaPlayer.create(SettingActivity.this,R.raw.alert4);
                mediaPlayer.setVolume(volume,volume);
                mediaPlayer.start();
            }
            else if(checkedId==R.id.radio_5){
                MediaPlayer mediaPlayer= MediaPlayer.create(SettingActivity.this,R.raw.alert5);
                mediaPlayer.setVolume(volume,volume);
                mediaPlayer.start();
            }
            else if(checkedId==R.id.radio_6){
                MediaPlayer mediaPlayer= MediaPlayer.create(SettingActivity.this,R.raw.alert6);
                mediaPlayer.setVolume(volume,volume);
                mediaPlayer.start();
            }
            else if(checkedId==R.id.radio_7){
                MediaPlayer mediaPlayer= MediaPlayer.create(SettingActivity.this,R.raw.alert7);
                mediaPlayer.setVolume(volume,volume);
                mediaPlayer.start();
            }
        }
    });
    }



}