package com.example.memorryaplicationproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.local.MemoryDocumentOverlayCache;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;


import model.MemoryObj;
import util.AppAPI;

public class PhotosActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private static final String TAG = "PhotosActivity";
    private ConstraintLayout photoLayout;
    private ConstraintLayout faceLayout;
    private Button faceSaveButton;
    private Button faceSkipButton;
    private ImageView faceImageView;
    private EditText currentFaceTextView;

    private Button saveButton;
    private ProgressBar progressBar;
    private ImageView addPhotoButton;
    private ImageView goToGalleryButton;
    private EditText titleEditText;
    private EditText thoughtsEditText;
    private TextView currentUserTextView;
    private ImageView imageView;
    FaceDetectorOptions  highAccuracyOpts;
    private String currentUserId;
    private String currentUserName;
    private Bitmap bitmap;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private Switch savePersons;
    private int faceIndex;
    private List<Face> savedFaces = null;

    //connection to firestore

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("Memories");


    private Uri imageUri;
    private int orientation = 0;
    private int degree = 0;
    private Matrix matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        faceIndex = 0;
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.post_progressBar);
        titleEditText = findViewById(R.id.post_title_et);
        thoughtsEditText = findViewById(R.id.post_description_et);
        currentUserTextView = findViewById(R.id.post_username_textview);
        savePersons = findViewById(R.id.saveFaces);
        photoLayout = findViewById(R.id.photo_layout);
        faceLayout = findViewById(R.id.face_layout);
        faceSaveButton = findViewById(R.id.post_save_face_button);
        faceSaveButton.setOnClickListener(this);
        faceSkipButton = findViewById(R.id.post_skip_face_button);
        faceSkipButton.setOnClickListener(this);
        currentFaceTextView = findViewById(R.id.post_person_et);
        storageReference = FirebaseStorage.getInstance().getReference();
        imageView = findViewById(R.id.post_imageView);
        saveButton = findViewById(R.id.post_save_photo_button);
        saveButton.setOnClickListener(this);
        addPhotoButton = findViewById(R.id.postCameraButton);
        addPhotoButton.setOnClickListener(this);
        goToGalleryButton = findViewById(R.id.galleyCameraButton);
        goToGalleryButton.setOnClickListener(this);
        faceImageView = findViewById(R.id.post_imageView_face);
        progressBar.setVisibility(View.INVISIBLE);

        highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setMinFaceSize(0.02f)
                        .build();

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
            case R.id.post_save_photo_button:
                //save Memory
                saveMemory();
                break;
            case R.id.galleyCameraButton:
                startActivity(new Intent(PhotosActivity.this,
                        PhotoGalleryActivity.class));
                finish();
                break;
            case R.id.postCameraButton:
                //add photo from phone
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
                break;
            case R.id.post_skip_face_button:
                //add photo from phone
                faceIndex++;

//                StorageReference facePath = filepath.child("face" + Timestamp.now().getSeconds()); //my_image_thetimestamp
//                facePath.putBytes(data);
//
                if (faceIndex >= savedFaces.size()) {
                    startActivity(new Intent(PhotosActivity.this,
                            PhotoGalleryActivity.class));
                    finish();
                } else {
                    Face face = savedFaces.get(faceIndex);
                    Rect bounds = face.getBoundingBox();
                    int width = bounds.right - bounds.left + 50;
                    int height = bounds.top - bounds.bottom + 50;
                    height = Math.max(width, height) + 50;

                    Bitmap resizedBmp = Bitmap.createBitmap(bitmap, bounds.left, bounds.top - 50, width, height, matrix, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resizedBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    faceImageView.setImageBitmap(resizedBmp);

//                    byte[] data = baos.toByteArray();

                }
                break;
            case R.id.post_save_face_button:
                saveFace();
                faceIndex++;

                if (faceIndex >= savedFaces.size()) {
                    startActivity(new Intent(PhotosActivity.this,
                            PhotoGalleryActivity.class));
                    finish();
                } else {
                    Face face = savedFaces.get(faceIndex);
                    Rect bounds = face.getBoundingBox();
                    int width = bounds.right - bounds.left + 50;
                    int height = bounds.top - bounds.bottom + 50;
                    height = Math.max(width, height) + 50;

                    Bitmap resizedBmp = Bitmap.createBitmap(bitmap, bounds.left, bounds.top - 50, width, height, matrix, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resizedBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    faceImageView.setImageBitmap(resizedBmp);


                }
                break;
        }
    }

    private void saveFace() {
        String name = currentFaceTextView.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            return;
        }
        StorageReference filepath = storageReference //.../memory_images/our_image.jpeg
                .child("memory_images");

        StorageReference imagePath = filepath.child("face_" + name + "_" + Timestamp.now().getSeconds()); //my_image_thetimestamp
        Face face = savedFaces.get(faceIndex);
        Rect bounds = face.getBoundingBox();
        int width = bounds.right - bounds.left + 50;
        int height = bounds.top - bounds.bottom + 50;
        height = Math.max(width, height) + 50;

        Bitmap resizedBmp = Bitmap.createBitmap(bitmap, bounds.left, bounds.top - 50, width, height, matrix, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        imagePath.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();


                        MemoryObj memoryObj = new MemoryObj();
                        memoryObj.setTitle(name);
                        memoryObj.setPictureName(imagePath.toString());
                        memoryObj.setImageUrl(imageUrl);
                        memoryObj.setTimeAdded(new Timestamp(new Date()));
                        memoryObj.setUserName(currentUserName);
                        memoryObj.setUserId(currentUserId);
                        collectionReference.add(memoryObj);
                    }
                });
            }
        });
//        faceImageView.setImageBitmap(resizedBmp);
    }

    private void saveMemory() {
        String title = titleEditText.getText().toString().trim();
        String memory = thoughtsEditText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(title) &&
        !TextUtils.isEmpty(memory) &&
        imageUri != null){

            StorageReference filepath = storageReference //.../memory_images/our_image.jpeg
                    .child("memory_images");
            StorageReference imagePath = filepath.child("my_image_" + Timestamp.now().getSeconds()); //my_image_thetimestamp

            Bitmap resizedBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            imagePath.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                             imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {

                                     String imageUrl = uri.toString();
                                     MemoryObj memoryObj = new MemoryObj();
                                     memoryObj.setTitle(title);
                                     memoryObj.setDescription(memory);
                                     memoryObj.setPictureName(imagePath.toString());
                                     memoryObj.setImageUrl(imageUrl);
                                     memoryObj.setTimeAdded(new Timestamp(new Date()));
                                     memoryObj.setUserName(currentUserName);
                                     memoryObj.setUserId(currentUserId);

                                     boolean savePersonsTrigger = savePersons.isChecked();
                                     if (savePersonsTrigger && savedFaces != null && savedFaces.size() > 0) {

                                         photoLayout.setVisibility(View.INVISIBLE);
                                         faceLayout.setVisibility(View.VISIBLE);

                                         Face face = savedFaces.get(faceIndex);
                                         Rect bounds = face.getBoundingBox();
                                         int width = bounds.right - bounds.left + 50;
                                         int height = bounds.top - bounds.bottom + 50;
                                         height = Math.max(width, height) + 50;

                                         Bitmap resizedBmp = Bitmap.createBitmap(bitmap, bounds.left, bounds.top - 50, width, height, matrix, true);
                                         faceImageView.setImageBitmap(resizedBmp);

                                     }
                                     else {
                                         collectionReference.add(memoryObj)
                                                 .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                     @Override
                                                     public void onSuccess(DocumentReference documentReference) {
                                                         if (photoLayout.getVisibility() == View.VISIBLE ) {
                                                             progressBar.setVisibility(View.INVISIBLE);
                                                             startActivity(new Intent(PhotosActivity.this,
                                                                     PhotoGalleryActivity.class));
                                                             finish();
                                                         }
                                                     }
                                                 })
                                                 .addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
                                                         Log.d(TAG, "onFailure" + e.getMessage());
                                                     }
                                                 });

                                     }

                                 }
                             });

                        }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         progressBar.setVisibility(View.INVISIBLE);

                     }
                 });

        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(PhotosActivity.this,
                    "Please enter valid data",
                    Toast.LENGTH_LONG)
                    .show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if(data != null){
                imageUri = data.getData(); //we have the actual path to the image
                try (InputStream inputStream = this.getContentResolver().openInputStream(imageUri)) {
                    ExifInterface exif = new ExifInterface(inputStream);
                    orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                    }
                    else if (orientation == 3) {
                        matrix.postRotate(180);
                    }
                    else if (orientation == 8) {
                        matrix.postRotate(270);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    InputImage image = InputImage.fromBitmap(bitmap, 0);
                    FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);

                    Task<List<Face>> result = detector.process(image)
                            .addOnSuccessListener(
                                    new OnSuccessListener<List<Face>>() {
                                        @Override
                                        public void onSuccess(List<Face> faces) {
                                            savedFaces = faces;
                                        }
                                    });

                } catch (FileNotFoundException e){

                } catch (IOException ex) {

                }
                imageView.setImageURI(imageUri);//show image

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(PhotosActivity.this,
                MainMenuActivity.class));
        finish();
    }
}