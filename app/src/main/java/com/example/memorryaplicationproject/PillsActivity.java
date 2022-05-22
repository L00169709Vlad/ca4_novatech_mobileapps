package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.MemoryObj;
import model.Pill;
import util.AppAPI;

public class PillsActivity extends AppCompatActivity  {
    LinearLayout pillList;
    Button addPillBtn;
    Button saveBtn;
    List<Pill> pills = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("Pills");
    private AlarmManager alarmMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pills);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        pillList = findViewById(R.id.linearLayout);
        addPillBtn = findViewById(R.id.addPillBtn);
        saveBtn = findViewById(R.id.ContinueToMainBtn);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Context context = this.getApplicationContext();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < pillList.getChildCount(); i++) {
                    View pillView = pillList.getChildAt(i);
                    EditText pillName = (EditText) pillView.findViewById(R.id.pill_name);
                    EditText pillHour = (EditText) pillView.findViewById(R.id.editTextPillHour);
                    EditText pillMinute = (EditText) pillView.findViewById(R.id.editTextPillMinute);

                    String name = pillName.getText().toString();
                    String hourString = pillHour.getText().toString();
                    String minuteString = pillMinute.getText().toString();
                    if (name.length() != 0 && hourString.length() != 0 && minuteString.length() != 0) {
                        int hour = Integer.valueOf(hourString);
                        int minute = Integer.valueOf(minuteString);

                        if (name.length() != 0 && hour < 24 && minute < 60) {
                            Pill currentPill = new Pill(name, hour, minute, AppAPI.getInstance().getUserId());
                            pills.add(currentPill);
                        }
                    }
                }

                int id = 100000;
                for (Pill pill :pills) {
                    collectionReference.add(pill);
                    calendar.set(Calendar.HOUR_OF_DAY, pill.getHour());
                    calendar.set(Calendar.MINUTE, pill.getMinute());
                    Intent my_intent = new Intent(context, AlarmReceiver.class);
                    my_intent.putExtra("name", pill.getName());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context, id, my_intent, 0);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            1000 * 60 * 20, pendingIntent);
                    id++;
                }

                startActivity(new Intent(PillsActivity.this, MainMenuActivity.class));
                finish();

            }
        });
        addPillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });
    }
    private void addView() {
        View pillView = getLayoutInflater().inflate(R.layout.row_add_pill, null, false);

        EditText pillName = (EditText)pillView.findViewById(R.id.pill_name);
        EditText pillHour = (EditText)pillView.findViewById(R.id.editTextPillHour);
        EditText pillMinute = (EditText) pillView.findViewById(R.id.editTextPillMinute);
        ImageView imageClose = (ImageView)pillView.findViewById(R.id.removePill);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(pillView);
            }
        });

        pillList.addView(pillView);
    }

    private void addViewWithItems(String name, int hour, int minute) {
        View pillView = getLayoutInflater().inflate(R.layout.row_add_pill, null, false);

        EditText pillName = (EditText)pillView.findViewById(R.id.pill_name);
        EditText pillHour = (EditText)pillView.findViewById(R.id.editTextPillHour);
        EditText pillMinute = (EditText) pillView.findViewById(R.id.editTextPillMinute);
        ImageView imageClose = (ImageView)pillView.findViewById(R.id.removePill);
        pillName.setText(name);
        pillHour.setText(String.valueOf(hour));
        pillMinute.setText(String.valueOf(minute));

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(pillView);
            }
        });

        pillList.addView(pillView);
    }
    private void removeView(View v) {
        pillList.removeView(v);
    }
    @Override
    protected void onStart() {
        super.onStart();
        List<String> listOfDocuments = new ArrayList<String>();
        List<Pill> listOfPills = new ArrayList<Pill>();

        collectionReference.whereEqualTo("userId", AppAPI.getInstance().
                getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot memories : queryDocumentSnapshots) {
                                listOfDocuments.add(memories.getId());
                                Pill pillObj = memories.toObject(Pill.class);
                                listOfPills.add(pillObj);
                            }
                        }

                        for (String document : listOfDocuments) {
                            db.collection("Pills").document(document)
                                    .delete();
                        }

                        for(Pill pill : listOfPills) {
                            addViewWithItems(pill.getName(), pill.getHour(), pill.getMinute());
                        }
                    }
                });

    };
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(PillsActivity.this,
                MainMenuActivity.class));
        finish();
    }
}