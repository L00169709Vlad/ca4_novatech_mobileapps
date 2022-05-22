package com.example.memorryaplicationproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import util.AppAPI;

public class FaceActivity extends AppCompatActivity implements View.OnClickListener{

    private byte[] data;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private EditText nameEditText;
    private Button saveButton;
    private Button skipButton;
    private ImageView imageView;
    private StorageReference storageReference;
    private String currentUserId;
    private String currentUserName;
    private TextView currentUserTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        Bundle b = getIntent().getExtras();
        if(b != null)
            data = b.getByteArray("photo");
        else {
            finish();
        }
        firebaseAuth = FirebaseAuth.getInstance();
        nameEditText = findViewById(R.id.post_person_et);
        saveButton = findViewById(R.id.post_save_face_button);
        saveButton.setOnClickListener(this);
        skipButton = findViewById(R.id.post_skip_face_button);
        skipButton.setOnClickListener(this);
        imageView = findViewById(R.id.post_imageView_face);
        storageReference = FirebaseStorage.getInstance().getReference();

        if(AppAPI.getInstance() != null){
            currentUserId = AppAPI.getInstance().getUserId();
            currentUserName = AppAPI.getInstance().getUsername();

            currentUserTextView.setText(currentUserName);

        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){

                }else{

                }
            }
        };

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.post_save_face_button:
                //save Memory
                saveMemory();
                break;
            case R.id.post_skip_face_button:
                finish();
                break;
        }
    }

    private void saveMemory() {
        String title = nameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            return;
        }
    }
}