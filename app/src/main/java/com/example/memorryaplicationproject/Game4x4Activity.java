package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.GridLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.MemoryObj;
import util.AppAPI;

public class Game4x4Activity extends AppCompatActivity implements View.OnClickListener {

    private int numberOfElements;

    private MemoryButton[] buttons;
    private List<MemoryObj> memoryObjList;

    private int[] buttonGraphicLocations;
    private int[] buttonGraphics;
    private Bitmap[] buttonBitmaps;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Integer> usedPhotos;
    private CollectionReference collectionReference = db.collection("Memories");

    private int correctMatches = 0;
    private boolean isBusy = false;
    int j = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4x4);

        firebaseAuth = FirebaseAuth.getInstance();

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout_4x4);

        int numColumns = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();
        usedPhotos = new ArrayList();
        numberOfElements = numRows * numColumns;

        buttons = new MemoryButton[numberOfElements];
        memoryObjList = new ArrayList<>();
        buttonBitmaps = new Bitmap[numberOfElements/2];
        buttonGraphics = new int[numberOfElements / 2];
        buttonGraphics[0] = R.drawable.button_1;
        buttonGraphics[1] = R.drawable.button_2;
        buttonGraphics[2] = R.drawable.button_3;
        buttonGraphics[3] = R.drawable.button_4;
        buttonGraphics[4] = R.drawable.button_5;
        buttonGraphics[5] = R.drawable.button_6;
        buttonGraphics[6] = R.drawable.button_7;
        buttonGraphics[7] = R.drawable.button_8;
        Context context = this;
        View.OnClickListener listener = this;
        collectionReference.whereEqualTo("userId", AppAPI.getInstance().
                getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot memories : queryDocumentSnapshots) {
                                MemoryObj memoryObj = memories.toObject(MemoryObj.class);
                                if(memoryObj != null) {
                                    memoryObjList.add(memoryObj);
                                }
                            }
                            Random rand = new Random(Timestamp.now().getSeconds()); //instance of random class
                            int i = 0;
                            MemoryObj memory = memoryObjList.get(0);
                            while (i < numberOfElements/2) {
                                int r = rand.nextInt(memoryObjList.size());
                                memory = memoryObjList.get(r);
                                if (memory.getPictureName() == null && !usedPhotos.contains(r)) {
                                    continue;
                                }
                                usedPhotos.add(r);

                                StorageReference imagePath = FirebaseStorage.getInstance().getReferenceFromUrl(memory.getPictureName());
                                final long ONE_MEGABYTE = 12 * 1024 * 1024;

                                imagePath.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        // Data for "images/island.jpg" is returns, use this as needed
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        buttonBitmaps[j] = bitmap;
                                        j++;
                                    }
                                });
                                i++;
                            }
                            buttonGraphicLocations = new int[numberOfElements];

                            shuffleButtonGraphics();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    for (int r = 0; r < numRows; r++) {
                                        for (int c = 0; c < numColumns; c++) {
                                            MemoryButton tempButton;
                                            if(buttonBitmaps[buttonGraphicLocations[r * numColumns + c] ]!= null) {
                                                tempButton  = new MemoryButton(context, r, c, buttonBitmaps[buttonGraphicLocations[r * numColumns + c]], 200, 200, buttonGraphicLocations[r * numColumns + c]);
                                            } else {
                                                tempButton = new MemoryButton(context, r, c, buttonGraphics[buttonGraphicLocations[r * numColumns + c]], 200, 200);
                                            }
                                            tempButton.setId(View.generateViewId());
                                            tempButton.setOnClickListener(listener);
                                            tempButton.setHeight(200);
                                            tempButton.setWidth(200);
                                            buttons[r * numColumns + c] = tempButton;
                                            gridLayout.addView(tempButton);
                                        }
                                    }
                                }
                            }, 4000);



                        } else {
                            buttonGraphicLocations = new int[numberOfElements];

                            shuffleButtonGraphics();

                            for (int r = 0; r < numRows; r++) {
                                for (int c = 0; c < numColumns; c++) {
                                    MemoryButton tempButton = new MemoryButton(context, r, c, buttonGraphics[buttonGraphicLocations[r * numColumns + c]], 200, 200);
                                    tempButton.setId(View.generateViewId());
                                    tempButton.setOnClickListener(listener);
                                    tempButton.setHeight(200);
                                    tempButton.setWidth(200);
                                    buttons[r * numColumns + c] = tempButton;
                                    gridLayout.addView(tempButton);
                                }
                            }
                        }
                    }
                });
    }
    protected void shuffleButtonGraphics(){

        Random r = new Random();

        for(int i = 0; i < numberOfElements; i++){
            buttonGraphicLocations[i] = i % (numberOfElements/2);
        }

        for(int i = 0; i < numberOfElements; i++){
            int temp = buttonGraphicLocations[i];

            int swapLocation = r.nextInt(16);

            buttonGraphicLocations[i] = buttonGraphicLocations[swapLocation];

            buttonGraphicLocations[swapLocation] = temp;
        }

    }

    @Override
    public void onClick(View view) {

//        if(isBusy){
//            return;
//        }

        MemoryButton button = (MemoryButton) view;

        if(button.isMatched){
            return;
        }

        if(selectedButton1 == null){
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }

        if(selectedButton1.getId() == button.getId()){
            return;
        }

        if(selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()){
            button.flip();
            button.setMatched(true);
            selectedButton1.setMatched(true);
            selectedButton1.setEnabled(false);
            button.setEnabled(false);
            selectedButton1 = null;
            correctMatches++;
            if (correctMatches == 8){
                finish();
            }
            return;

        }else{
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton1 = null;
                    selectedButton2 = null;
                    isBusy = false;
                }
            },500);

        }

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(Game4x4Activity.this,
                ConcentrationActivity.class));
        finish();
    }
}