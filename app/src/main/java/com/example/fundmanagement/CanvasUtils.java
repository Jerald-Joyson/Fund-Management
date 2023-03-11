package com.example.fundmanagement;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CanvasUtils {
    public static void drawText(Canvas canvas, String text, float x, float y, int color, int size) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(size);
        canvas.drawText(text, x, y, paint);
    }
}
