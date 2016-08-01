package andrewlt.fuel.Utils;

import java.text.DecimalFormat;

/**
 * Created by liut1 on 7/31/16.
 */
public class MyFormat {
    public Float decimalKeep(Float data, int count){
        DecimalFormat df = new DecimalFormat("0.00");
        return  Float.valueOf(df.format(data));
    }
}
