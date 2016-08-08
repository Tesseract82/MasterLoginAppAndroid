package com.example.unit271.loginmasterapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;

/**
 * Created by unit271 on 8/4/16.
 */
public class ScaleView extends View {

    private int linearLayoutHeight;
    private double maxMinutes, reductionFactor;
    private String units;

    public ScaleView(Context context, int linearLayoutHeight, int maxMinutes, String units){
        super(context);
        this.linearLayoutHeight = linearLayoutHeight;
        this.maxMinutes = maxMinutes;
        this.units = units;
        reductionFactor = 1;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(40);
        String pattern = ".##";
        String abbr = " N";
        if(units.equals("Minutes")){
            abbr = " m";
            reductionFactor = 1;
        } else if(units.equals("Hours")){
            abbr = " h";
            reductionFactor = 60;
            Log.i("REDUCTIONFACTOR", reductionFactor + "");
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        canvas.drawRect(100, 0, 150, 10, paint);
        canvas.drawRect(100, (linearLayoutHeight / 4) - 5, 150, (linearLayoutHeight / 4) + 5, paint);
        canvas.drawRect(100, (linearLayoutHeight / 2) - 5, 150, (linearLayoutHeight / 2) + 5, paint);
        canvas.drawRect(100, (3 * linearLayoutHeight / 4) - 5, 150, (3 * linearLayoutHeight / 4) + 5, paint);
        canvas.drawRect(100, linearLayoutHeight - 10, 150, linearLayoutHeight, paint);
        canvas.drawRect(100, 0, 110, linearLayoutHeight, paint);
        paint.setColor(Color.MAGENTA);
        canvas.drawText(decimalFormat.format(maxMinutes * (1 / reductionFactor)) + abbr, 0, linearLayoutHeight / 20, paint);
        canvas.drawText(decimalFormat.format(3 * (1 / reductionFactor) * maxMinutes / 4) + abbr, 0, linearLayoutHeight / 4, paint);
        canvas.drawText(decimalFormat.format(maxMinutes * (1 / reductionFactor) / 2) + abbr, 0, linearLayoutHeight / 2, paint);
        canvas.drawText(decimalFormat.format((1 / reductionFactor) * maxMinutes / 4) + abbr, 0, 3 * linearLayoutHeight / 4, paint);
        canvas.drawText("0.0" + abbr, 0, linearLayoutHeight, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        setMeasuredDimension(150, linearLayoutHeight);
    }
}
