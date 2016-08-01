package andrewlt.fuel.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liut1 on 7/28/16.
 */
public class MyDateTime {

    /*@param:
     last: the day before or after today
      */
    public String getLastDate(int last){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,last);
        Date date = new Date();
        date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(date);
    }
    public String getCurrentYear(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Log.e("===",new Date().toString());
        return format.format(new Date());
    }
    public String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Log.e("===",new Date().toString());
        return format.format(new Date());
    }
    public long getDaysBetween(String dayFrom, String dayTo){
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        long to = 0;
        try {
            to = df.parse(dayTo).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long from = 0;
        try {
            from = df.parse(dayFrom).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (to - from) / (1000 * 60 * 60 * 24);
    }
}
