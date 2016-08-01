package andrewlt.fuel.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;

import andrewlt.fuel.AddRecordActivity;
import andrewlt.fuel.DaoDataBase.MyFuelDetail;
import andrewlt.fuel.MyView.CurveView;
import andrewlt.fuel.MyView.WidgetTopBar;
import andrewlt.fuel.R;
import andrewlt.fuel.Utils.MyDateTime;
import andrewlt.fuel.Utils.MyFormat;
import de.greenrobot.dao.query.QueryBuilder;
import me.fuel.greendao.FuelData;
import me.fuel.greendao.FuelDataDao;

/**
 * Created by liut1 on 7/16/16.
 */
public class FuelCurve extends Fragment {
    private View view;
    private ImageButton mImageButton;
    private TextView mTextView;
    private MyFuelDetail mMyFuelDetail;
    private FuelDataDao mFuelDataDao;
    private CurveView mCurveView;
    private int year=0;
    private int month = 0;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.curve_chart, container, false);

        //init dao handle
        mMyFuelDetail = new MyFuelDetail().getInstance();
        mFuelDataDao = mMyFuelDetail.getFuelDetailDao();
        MyDateTime myDateTime = new MyDateTime();
        year = Integer.valueOf(myDateTime.getCurrentYear());
        for(int i=0;i<5;i++){
            List<FuelData> data;
            data = mFuelDataDao.queryBuilder().where(FuelDataDao.Properties.Year.eq(year)).list();
            if(data.size()>0){
                break;
            }
            year--;
        }
        mImageButton = (ImageButton)view.findViewById(R.id.ib_add);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "增加数据", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AddRecordActivity.class);
                FuelData fuelData = new FuelData();
                fuelData.setId((long) -1);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) fuelData);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mTextView = (TextView)view.findViewById(R.id.tv_add);

        mCurveView = (CurveView)view.findViewById(R.id.curveChart);
        setCoordinateData();
        setCurveChartData();
        setCurveDetail();
        return view;
    }
    //if there is no datas yet, display it
    public void ibAddSetVisible(){
        mImageButton.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.VISIBLE);
    }
    //if get any data, hide it
    public void ibAddSetInvisible(){
        mImageButton.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
    }

    private void setCoordinateData(){
        List<FuelData> daoData = null;
        daoData = mFuelDataDao.queryBuilder().where(FuelDataDao.Properties.Year.eq(year)).orderDesc(FuelDataDao.Properties.Month).list();

        if(daoData.size()>0){
            month = daoData.get(0).getMonth();
            mCurveView.setXCount(year,month);
        }else {
            List<String> xData = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                xData.add(String.valueOf(i + 1));
            }
            mCurveView.setxCount(xData);
        }

        QueryBuilder qb = mFuelDataDao.queryBuilder().where(FuelDataDao.Properties.Year.eq(year)).
                orderAsc(FuelDataDao.Properties.AverageFuel);
        daoData = qb.list();
        Log.e("=====","size="+mFuelDataDao.queryBuilder().list().size());
        if(qb.list().size()>0){
            float hightestFuel=0, lowestFuel=0;
            for(int j=0;j<daoData.size();j++){
                if(!daoData.get(j).getAverageFuel().equals(0f)){
                    lowestFuel = daoData.get(j).getAverageFuel();
                    break;
                }
            }
            for(int j=daoData.size()-1;j>=0;j--){
                if(!daoData.get(j).getAverageFuel().equals(0f)){
                    hightestFuel = daoData.get(j).getAverageFuel();
                    break;
                }
            }
            mCurveView.setyData(lowestFuel,hightestFuel);
        }
    }
    public void setCurveChartData(){
        List<FuelData> daoData = null;
        QueryBuilder qb = mFuelDataDao.queryBuilder().where(FuelDataDao.Properties.Year.eq(year)).
                orderAsc(FuelDataDao.Properties.AverageFuel);
        daoData = qb.list();
        Log.e("=====","size="+mFuelDataDao.queryBuilder().list().size());
        if(qb.list().size()>0){

//            Log.e("=====","target="+targetMonth);
            daoData = mFuelDataDao.queryBuilder().list();
            List<Map<String,Float>> cData = new ArrayList<>();
            for(int i=0;i<daoData.size();i++){
//                Log.e("=======",daoData.get(i).getMonth()+" yue  "+daoData.get(i).getAverageFuel());
                Map<String,Float> map = new HashMap<>();
                map.put("year",(float) daoData.get(i).getYear());
                map.put("month",(float) daoData.get(i).getMonth());
                map.put("day",(float) daoData.get(i).getDay());
                map.put("fuel",daoData.get(i).getAverageFuel());
//                Log.e("========",daoData.size()+":month="+i+":size="+daoData.size());
                cData.add(map);
            }
            mCurveView.setcData(cData);
            if(cData.size()>0){
                ibAddSetInvisible();
            }
        }

    }

    public void setCurveDetail(){
        TextView tv;
        Float tmp = 0f;
        float averageFuel=0f, hightestFuel=0f, lowestFuel=0f;
        float latestFuel=0, currentMiles=0, totalMiles=0, totalFuel=0, averageMiles=0;
        List<FuelData> daoData = null;
        MyFormat myFormat = new MyFormat();
        QueryBuilder qb = mFuelDataDao.queryBuilder().where(FuelDataDao.Properties.Year.eq(year)).
                orderAsc(FuelDataDao.Properties.AverageFuel);
        daoData = qb.list();
        if(daoData.size()>0){
            for(int j=0;j<daoData.size();j++){
//                Log.e("====","average:"+daoData.get(j).getAverageFuel());
                if(!daoData.get(j).getAverageFuel().equals(0f)){
                    lowestFuel = daoData.get(j).getAverageFuel();
//                    Log.e("=======","lowestFuel:"+lowestFuel);
                    break;
                }
            }
            for(int j=daoData.size()-1;j>=0;j--){
                if(!daoData.get(j).getAverageFuel().equals(0f)){
                    hightestFuel = daoData.get(j).getAverageFuel();
//                    Log.e("=======","hightestFuel:"+hightestFuel);
                    break;
                }
            }
            tv = (TextView)view.findViewById(R.id.lowest_fuel);
            tv.setText(myFormat.decimalKeep(lowestFuel,2)+" 升/百公里");
            tv = (TextView)view.findViewById(R.id.highest_fuel);
            tv.setText(myFormat.decimalKeep(hightestFuel,2)+" 升/百公里");
            for(int i=0;i<daoData.size();i++){
//                Log.e("=======","i:"+i);
                if(daoData.get(i).getUnitPrice() != null && !daoData.get(i).getUnitPrice().equals(0)) {
//                    Log.e("=======","getUnitPrice:"+daoData.get(i).getUnitPrice());
                    totalFuel += (daoData.get(i).getTotalPrice() / daoData.get(i).getUnitPrice());
                }
            }

            tv = (TextView)view.findViewById(R.id.consume_fuel);
            tv.setText(myFormat.decimalKeep(totalFuel,2)+" 升");
        }

        qb = mFuelDataDao.queryBuilder().where(FuelDataDao.Properties.Year.eq(year)).
                orderAsc(FuelDataDao.Properties.Month);
        daoData = qb.list();
        if(daoData.size()>0){
            if(daoData.get(daoData.size()-1).getAverageFuel() != null) {
                latestFuel = daoData.get(daoData.size() - 1).getAverageFuel();
            }
            tv = (TextView)view.findViewById(R.id.latest_fuel);
            tv.setText(myFormat.decimalKeep(latestFuel,2)+" 升/百公里");
            if(daoData.get(daoData.size()-1).getCurrentMiles() != null) {
                currentMiles = daoData.get(daoData.size() - 1).getCurrentMiles();
            }
            tv = (TextView)view.findViewById(R.id.current_miles);
            tv.setText(myFormat.decimalKeep(currentMiles,2)+" 公里");
            totalMiles = currentMiles;
            tv = (TextView)view.findViewById(R.id.record_miles);
            tv.setText(myFormat.decimalKeep(totalMiles,2)+" 公里");
        }

        daoData = mFuelDataDao.queryBuilder().list();
        int count = 0;
        float averages = 0f;
        for(int i=0;i<daoData.size();i++){
            if(!daoData.get(i).getAverageFuel().equals(0f)){
                averages += daoData.get(i).getAverageFuel();
                count++;
            }
        }
        averageFuel = averages/count;
        tv = (TextView)view.findViewById(R.id.average_fuel);
        tv.setText(myFormat.decimalKeep(averageFuel,2)+" 升/百公里");


        for(int i=daoData.size()-1;i>=0;i--){
            if(!daoData.get(i).getAverageFuel().equals(0f)){
                tv=(TextView)view.findViewById(R.id.latest_fuel);
                tv.setText(myFormat.decimalKeep(daoData.get(i).getAverageFuel(),2)+" 升/百公里");

                tv=(TextView)view.findViewById(R.id.record_miles);
                tv.setText(daoData.get(i).getCurrentMiles()+"");

                tv=(TextView)view.findViewById(R.id.average_miles);
                String dayFrom,dayTo;
                long days = 0;
                MyDateTime date = new MyDateTime();
                dayFrom = daoData.get(0).getYear()+"/"+daoData.get(0).getMonth()+"/"+daoData.get(0).getDay();
                dayTo = date.getLastDate(0);
                days = date.getDaysBetween(dayFrom,dayTo)+1;
                Log.e("-=====","from="+dayFrom+"  to="+dayTo+"   miles="+daoData.get(i).getCurrentMiles()+"  days="+days);
                tv.setText(daoData.get(i).getCurrentMiles()/days+"");
                break;
            }
        }
    }
}
