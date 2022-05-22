package com.example.memorryaplicationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import model.MemoryObj;
import ui.RecyclerAdapter;
import util.AppAPI;

public class PhotoGalleryActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private List<MemoryObj> memoryObjList;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    private CollectionReference collectionReference = db.collection("Memories");
    private TextView noMemoryEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        noMemoryEntry = findViewById(R.id.list_no_thoughts);

        memoryObjList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                //take user to add more photos
                if(user !=null && firebaseAuth !=null){
                    startActivity(new Intent(PhotoGalleryActivity.this, PhotosActivity.class));
                   finish();
                }
                break;
        }


        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.whereEqualTo("userId", AppAPI.getInstance().
                getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot memories : queryDocumentSnapshots){
                                MemoryObj memoryObj = memories.toObject(MemoryObj.class);
                                memoryObjList.add(memoryObj);
                            }

                            //Invoke recyclerview
                            recyclerAdapter = new RecyclerAdapter(PhotoGalleryActivity.this,
                                    memoryObjList);
                            recyclerView.setAdapter(recyclerAdapter);
                            recyclerAdapter.notifyDataSetChanged();

                        }else{

                            noMemoryEntry.setVisibility(View.VISIBLE);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
}