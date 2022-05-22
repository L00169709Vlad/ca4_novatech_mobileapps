package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageRegistrar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import model.MemoryObj;
import ui.RecyclerAdapter;
import util.AppAPI;

public class PuzzleGame extends AppCompatActivity {
    private  ArrayList<Piece> pieces;
    private RelativeLayout layout;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private MemoryObj memory;
    private TextView memoryText;
    private TextView memoryText1;
    private TextView memoryText2;

    private Button restartButton;
    private Button goBackButton;

    private List<MemoryObj> memoryObjList;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    private CollectionReference collectionReference = db.collection("Memories");
    private TextView noMemoryEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_game);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        noMemoryEntry = findViewById(R.id.list_no_thoughts);
        memoryText = findViewById(R.id.text_congratulation);
        restartButton = findViewById(R.id.restart);
        goBackButton = findViewById(R.id.back);
        memoryText1 = findViewById(R.id.text_congratulation2);
        memoryText2 = findViewById(R.id.text_congratulation3);

        goBackButton.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.INVISIBLE);
        memoryText.setVisibility(View.INVISIBLE);
        memoryText1.setVisibility(View.INVISIBLE);
        memoryText2.setVisibility(View.INVISIBLE);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        memoryObjList = new ArrayList<>();


        layout = findViewById(R.id.layout);
        imageView = findViewById(R.id.puzzle_image);

        imageView.post(new Runnable() {
            @Override
            public void run() {
                collectionReference.whereEqualTo("userId", AppAPI.getInstance().
                        getUserId())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot memories : queryDocumentSnapshots) {
                                        MemoryObj memoryObj = memories.toObject(MemoryObj.class);
                                        memoryObjList.add(memoryObj);
                                    }
                                    Random rand = new Random(Timestamp.now().getSeconds()); //instance of random class
                                    while(true) {
                                        int i = rand.nextInt(memoryObjList.size());
                                        memory = memoryObjList.get(i);
                                        if (memory.getPictureName() != null && memory.getPictureName().contains("my_image")) {
                                            break;
                                        }
                                    }

                                    StorageReference imagePath = FirebaseStorage.getInstance().getReferenceFromUrl(memory.getPictureName());
                                    final long ONE_MEGABYTE = 12 * 1024 * 1024;

                                    imagePath.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            // Data for "images/island.jpg" is returns, use this as needed
                                            Log.d("AA","b");
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                            imageView.setImageBitmap(bitmap);//show image
                                            Log.d("AA","c" + bitmap.getHeight());

                                            pieces = splitImage();
                                            Collections.shuffle(pieces);

                                            TouchListener touchListener = new TouchListener();

                                            for(Piece piece : pieces) {
                                                piece.setOnTouchListener(touchListener);
                                                layout.addView(piece);

                                                // randomize position, on the bottom of the screen
                                                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                                                lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                                                lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                                                piece.setLayoutParams(lParams);
                                            }
                                        }
                                    });

                                }
                            }
                        });
            }
        });
    }

    public void checkGameOver() {
        if (isGameOver()) {
            memoryText.setVisibility(View.VISIBLE);
            memoryText1.setVisibility(View.VISIBLE);
            memoryText2.setVisibility(View.VISIBLE);
            restartButton.setVisibility(View.VISIBLE);
            goBackButton.setVisibility(View.VISIBLE);
            String description = "";
            if (memory.getDescription() != null) {
                description = memory.getTitle();
            } else {
                description = memory.getDescription();
            }
            memoryText.setText("Awesome");
            memoryText1.setText("This is a photo of " + description);
            memoryText2.setText("Would you like to try another puzzle?");
        }
    }

    private boolean isGameOver() {
        for (Piece piece : pieces) {
            if (piece.canMove) {
                return false;
            }
        }

        return true;
    }
    private ArrayList<Piece> splitImage() {
        int rows = 4;
        int cols = 3;
        int numberOfPieces = rows * cols;

        ImageView imageView = findViewById(R.id.puzzle_image);
        ArrayList<Piece> pieces = new ArrayList<>(numberOfPieces);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * Math.abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * Math.abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, Math.abs(scaledBitmapLeft), Math.abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        // Calculate the with and height of the pieces
        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;
        int yCoord = 0;

        for (int i = 0; i < rows; i++) {
            int xCoord = 0;
            for (int j = 0; j < cols; j++) {
                int offsetX = 0;
                int offsetY = 0;
                if (j > 0) {
                    offsetX = pieceWidth / 3;
                }
                if (i > 0) {
                    offsetY = pieceHeight / 3;
                }
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord - offsetX, yCoord - offsetY, pieceWidth + offsetX, pieceHeight + offsetY);
                Piece piece = new Piece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord - offsetX + imageView.getLeft();
                piece.yCoord = yCoord - offsetY + imageView.getTop();
                piece.pieceWidth = pieceWidth + offsetX;
                piece.pieceHeight = pieceHeight + offsetY;
                Bitmap puzzlePiece = Bitmap.createBitmap(pieceWidth + offsetX, pieceHeight + offsetY, Bitmap.Config.ARGB_8888);

                int bumpSize = pieceHeight / 4;
                Canvas canvas = new Canvas(puzzlePiece);
                Path path = new Path();
                path.moveTo(offsetX, offsetY);
                if (i == 0) {
                    // top side piece
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                } else {
                    // top bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3, offsetY);
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, offsetY);
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                }

                if (j == cols - 1) {
                    // right side piece
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                } else {
                    // right bump
                    path.lineTo(pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.cubicTo(pieceBitmap.getWidth() - bumpSize,offsetY + (pieceBitmap.getHeight() - offsetY) / 6, pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                }

                if (i == rows - 1) {
                    // bottom side piece
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                } else {
                    // bottom bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, pieceBitmap.getHeight());
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5,pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6, pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3, pieceBitmap.getHeight());
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                }

                if (j == 0) {
                    // left side piece
                    path.close();
                } else {
                    // left bump
                    path.lineTo(offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.cubicTo(offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6, offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.close();
                }

                Paint paint = new Paint();
                paint.setColor(0XFF000000);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawPath(path, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(pieceBitmap, 0, 0, paint);

                // draw a white border
                Paint border = new Paint();
                border.setColor(0X80FFFFFF);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(8.0f);
                canvas.drawPath(path, border);

                border = new Paint();
                border.setColor(0X80000000);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(3.0f);
                canvas.drawPath(path, border);

                piece.setImageBitmap(puzzlePiece);

                pieces.add(piece);
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        return pieces;
    }

    private int[] getBitmapPositionInsideImageView(ImageView puzzleImage) {
        int[] result = new int[4];
        if(puzzleImage == null || puzzleImage.getDrawable() == null) {
            return result;
        }

        float[] f = new float[9];
        puzzleImage.getImageMatrix().getValues(f);

        float scaleX = f[Matrix.MSCALE_X];
        float scaleY = f[Matrix.MSCALE_Y];

        Drawable d = imageView.getDrawable();
        int width = d.getIntrinsicWidth();
        int height = d.getIntrinsicHeight();

        int newWidth = Math.round(width * scaleX);
        int newHeight = Math.round(height * scaleY);

        int imageWidth = puzzleImage.getWidth();
        int imageHeight = puzzleImage.getHeight();

        int left =  Math.round((imageWidth - newWidth)/2);
        int top =  Math.round((imageHeight - newHeight)/2);

        result[0] = left;
        result[1] = top;
        result[2] = newWidth;
        result[3] = newHeight;

        return result;
    }

    public class TouchListener implements View.OnTouchListener {
        private float xDelta;
        private float yDelta;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float x = motionEvent.getRawX();
            float y = motionEvent.getRawY();
            final double tolerance = Math.sqrt(Math.pow(view.getWidth(), 2) + Math.pow(view.getHeight(), 2)) / 10;

            Piece piece = (Piece) view;
            if (!piece.canMove) {
                return true;
            }
            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    xDelta = x - lParams.leftMargin;
                    yDelta = y - lParams.topMargin;
                    piece.bringToFront();

                    break;
                case MotionEvent.ACTION_MOVE:
                    lParams.leftMargin = (int) (x - xDelta);
                    lParams.topMargin = (int) (y - yDelta);
                    view.setLayoutParams(lParams);
                    break;
                case MotionEvent.ACTION_UP:
                    int xDiff = Math.abs(piece.xCoord - lParams.leftMargin);
                    int yDiff = Math.abs(piece.yCoord - lParams.topMargin);
                    if (xDiff <= tolerance && yDiff <= tolerance) {
                        lParams.leftMargin = piece.xCoord;
                        lParams.topMargin = piece.yCoord;
                        piece.setLayoutParams(lParams);
                        piece.canMove = false;
                        sendViewToBack(piece);
                        checkGameOver();
                    }
                    break;
            }

            return true;
        }
    }

    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(PuzzleGame.this,
                GamesActivity.class));
        finish();
    }

}