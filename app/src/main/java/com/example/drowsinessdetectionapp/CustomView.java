package com.example.drowsinessdetectionapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.View;

import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;

import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;

import java.util.List;

public class CustomView extends View {

    private ShapeDrawable drawable;
    Paint borderPaint;
    Rect rect;
    Face face;
    PreviewView preview;
    Boolean inPipMode;

    public CustomView(Context context, Rect rect, Face face, PreviewView preview,Boolean inPipMode) {
        super(context);
        this.rect=rect;
        this.face=face;
        this.preview=preview;
        borderPaint=new Paint();
        borderPaint.setColor(Color.GREEN);
        if(inPipMode) {
            borderPaint.setStrokeWidth(5f);
        }
        else{
            borderPaint.setStrokeWidth(10f);
        }
        borderPaint.setStyle(Paint.Style.STROKE);
        this.inPipMode=inPipMode;
        // If the color isn't set, the shape uses black as the default.
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       if (!this.inPipMode) {
            canvas.drawRect(getWidth() - rect.right, rect.top, getWidth() - rect.left, rect.bottom, borderPaint);
        }
        else{
            canvas.drawRect(getWidth()-(float)((rect.centerX()*0.3+(rect.right-rect.left)*0.2)), (float)(rect.top*0.15), getWidth()-(float)((rect.centerX()*0.4-(rect.right-rect.left)*0.2)),(float)(rect.bottom*0.25),borderPaint );
        }
    }

}
