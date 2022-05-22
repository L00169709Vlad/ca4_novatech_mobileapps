package com.example.memorryaplicationproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer mediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("rec", intent.getStringExtra("name"));

        Toast.makeText(context, " Time to take your " + intent.getStringExtra("name"), Toast.LENGTH_LONG).show();

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        MediaPlayer mp = MediaPlayer.create(context, notification);
        mp.start();
    }
}