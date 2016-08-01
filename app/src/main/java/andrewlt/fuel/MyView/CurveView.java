package andrewlt.fuel.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

import andrewlt.fuel.R;

/**
 * Created by liut1 on 7/16/16.
 */
public class CurveView extends View{
    private int baseCoordinateColor;
    private int contentDataColor;
    private int contentLineColor;
    private float baseCoordinateLineWidth;
    //how many cuts
    private Integer yDataMin=1,yDataMax=3;
    //the detail coordinate(x,y) to display,odd is x, even is y
    private List<Map<String,Float>> cData;
    //the detail data for x-coordinate
    private List<String> xData;
    public CurveView(Context context) {
//        super(context);
        this(context, null);
    }

    public CurveView(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    public CurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CurveView);
        baseCoordinateColor = mTypedArray.getColor(R.styleable.CurveView_baseCoordinateColor, Color.BLACK);
        contentDataColor = mTypedArray.getColor(R.styleable.CurveView_contentDataColor,Color.YELLOW);
        contentLineColor = mTypedArray.getColor(R.styleable.CurveView_contentLineColor,Color.RED);
        baseCoordinateLineWidth = mTypedArray.getFloat(R.styleable.CurveView_baseCoordinateLineWidth,6);
//        Log.e("------","baseCoordinateColor:"+baseCoordinateColor+"   baseCoordinateLineWidth:"+baseCoordinateLineWidth);
        mTypedArray.recycle();
    }
    @Override
    public void onDraw(Canvas canvas){
        Integer w = getWidth();
        Integer h = getHeight();
        Integer xStart = 100;
        Integer yStart = h-80;
        Integer xEndx = xStart + w -200;
        Integer xEndy = yStart;
        Integer yEndx = xStart;
        Integer yEndy = yStart - h + 160;

        Paint paint = new Paint();
        paint.setColor(baseCoordinateColor);
        //set line width
        paint.setStrokeWidth(baseCoordinateLineWidth);

        //draw x ray
        canvas.drawLine(xStart,yStart,xEndx,xEndy,paint);
        canvas.drawLine(xEndx-30,xEndy-30,xEndx,xEndy,paint);
        canvas.drawLine(xEndx-30,xEndy+30,xEndx,xEndy,paint);
        //draw y ray
        canvas.drawLine(xStart,yStart,yEndx,yEndy,paint);
        canvas.drawLine(yEndx-30,yEndy+30,yEndx,yEndy,paint);
        canvas.drawLine(yEndx+30,yEndy+30,yEndx,yEndy,paint);

        //draw x ray detail
        paint.setTextSize(40);
        Integer xDiff = (w-200)/xData.size();
        for(int i=0;i<xData.size();i++){
            canvas.drawLine(xStart+xDiff*i,yStart,xStart+xDiff*i,yStart+10,paint);
            String[] ss = xData.get(i).split("/");
            String text = ss[1];
            if(text.equals("1")){
                text = ss[0];
            }
            float textWidth = paint.measureText(text);
            canvas.drawText(text,xStart+xDiff*i-textWidth/2,yStart+60,paint);
        }

        //draw y ray detal
        int count = yDataMax+yDataMin-1;
        for(int i=0;i<count;i++) {
            canvas.drawLine(xStart, yStart-(yStart*i/count), xStart - 10, yStart-(yStart*i/count), paint);
            canvas.drawText(String.valueOf(yDataMin+i), xStart - 60, yStart-(yStart*i/count) + 25, paint);
        }


        if(cData ==null || cData.size() == 0){
            return;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(contentDataColor);
        float lx=0,ly=0;
        for(int i=0;i<cData.size();) {
            Map<String, Float> map = cData.get(i);
            if(map.get("fuel").intValue() > 0) {
                int year = map.get("year").intValue();
                int moth = map.get("month").intValue();
                int day = map.get("day").intValue();
//            Log.e("=====data===",year+"/"+moth+"/"+day);
                for(int j=0;j<xData.size();j++){

//                    Log.e("========",xData.get(j));
                    if(xData.get(j).equals(year+"/"+moth)){
                        float x = xStart + xDiff*j+xDiff*day/31;
                        float y = yStart - (map.get("fuel")-yDataMin)*(yStart-yEndy)/(yDataMax+yDataMin-1);
                        canvas.drawCircle(x, y, 5, paint);
                        if(i>0){
//                    Log.e("=====line===","lx="+lx+":ly="+ly+":x="+x+":y="+y);
                            canvas.drawLine(lx,ly,x,y,paint);
                        }
                        lx = x;
                        ly = y;
//                        Log.e("=====data===",map.get("month")+ ":x=" + xStart + xDiff * (map.get("month") - 1) + "   :y=" + (yStart) * map.get("fuel") / yDataMax);

                        break;
                    }
                }

            }
            i++;
        }

        //link the points

    }
    public void setxCount(List<String> xd){
        xData = xd;
    }
    public void setXCount(int year, int month){
        Log.e("====","setXCount="+year+"/"+month);
        if(xData == null){
            xData = new ArrayList<>();
        }
        List<String> data = new ArrayList<>();
        int m = month;
        int y = year;
        for(int i=11;i>=0;i--){
            if(m == 1){
                m = 13;
                y = y-1;
                data.add((y+1)+"/"+1);
            }else {
                data.add(y+"/"+m);
            }
            m--;
        }
        for(int i=11;i>=0;i--){
            Log.e("===","nianyue="+data.get(i));
            xData.add(data.get(i));
        }
    }
    public void setyData(Float ydMin,Float ydMax){
        if(ydMax == null){
            ydMax = 0f;
        }
        if(ydMin == null){
            ydMin = 0f;
        }
        yDataMax = ydMax.intValue()+1;
        yDataMin = ydMin.intValue();
        Log.e("==111====","yDataMax="+yDataMax+":yDataMin="+yDataMin);

        invalidate();
    }
    public void setcData(List<Map<String,Float>> data){
        cData = data;
        Log.e("====",((new Exception()).getStackTrace())[1].getMethodName()+":"+((new Exception()).getStackTrace())[1].getLineNumber());
        invalidate();
    }
}
