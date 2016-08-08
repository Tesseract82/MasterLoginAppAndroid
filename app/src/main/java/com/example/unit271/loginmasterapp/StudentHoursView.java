package com.example.unit271.loginmasterapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.text.DecimalFormat;

/**
 * Created by unit271 on 7/17/16.
 */
public class StudentHoursView extends View {

    private int height;
    private double minutes;
    private boolean hours;
    private String units;

    public StudentHoursView(Context context, int height, boolean hours, int minutes, String units){
        super(context);
        this.height = height;
        this.hours = hours;
        this.minutes = minutes;
        this.units = units;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        canvas.drawRect(50, 0, 150, height, paint);
        paint.setColor(Color.MAGENTA);
        paint.setTextSize(25);
        String abbr = "N";
        String pattern = ".##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        if(units.equals("Minutes")){
            abbr = "m";
        } else if(units.equals("Hours")){
            abbr = "h";
            minutes /= 60;
        }
        if(hours){
            if(height > 50) {
                canvas.drawText(decimalFormat.format(minutes) + abbr, 60, 50, paint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        setMeasuredDimension(200, height);
    }
}
