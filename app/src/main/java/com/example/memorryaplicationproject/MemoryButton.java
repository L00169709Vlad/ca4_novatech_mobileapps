package com.example.memorryaplicationproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.widget.GridLayout;

public class MemoryButton extends androidx.appcompat.widget.AppCompatButton {

    protected int row;
    protected int column;
    protected int frontDrawableId;
    protected Bitmap bitmap;
    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    public MemoryButton (Context context, int r, int c, int frontImageDrawableID, int width, int height){

        super(context);

        row = r;
        column = c;
        frontDrawableId = frontImageDrawableID;

        front = context.getDrawable(frontImageDrawableID);
        back = context.getDrawable(R.drawable.button_question_mark);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r),GridLayout.spec(c));

        tempParams.width = width;
        tempParams.height = height;

        setLayoutParams(tempParams);

    }

    public MemoryButton (Context context, int r, int c, Bitmap bitmap, int width, int height, int frontImageDrawableID){

        super(context);

        row = r;
        column = c;
        bitmap = bitmap;
        this.frontDrawableId = frontImageDrawableID;

        front = new BitmapDrawable(getResources(), bitmap);

        back = context.getDrawable(R.drawable.button_question_mark);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r),GridLayout.spec(c));

        tempParams.width = width;
        tempParams.height = height;

        setLayoutParams(tempParams);

    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }
    public int getBitmap() {
        return frontDrawableId;
    }

    public void flip(){
        if (isMatched) {
            return;
        }

        if (isFlipped){
            setBackground(back);
            isFlipped = false;
        } else {
            setBackground(front);
            isFlipped = true;
        }
    }
}
