package com.example.memorryaplicationproject;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Piece  extends AppCompatImageView {
    public int xCoord;
    public int yCoord;
    public int pieceWidth;
    public int pieceHeight;
    public boolean canMove = true;

    public Piece(Context context) {
        super(context);
    }
}
